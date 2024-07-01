package unibuc.ro.ParkingApp.service;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unibuc.ro.ParkingApp.configuration.ApplicationConstants;
import unibuc.ro.ParkingApp.model.listing.Listing;
import unibuc.ro.ParkingApp.model.listing.ListingRentalDetails;
import unibuc.ro.ParkingApp.repository.ListingRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class CronJobService {
    private static final Logger log = LoggerFactory.getLogger(CronJobService.class);
    private ListingRepository listingRepository;
    private ChatService chatService;
    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void onStartup() {
        updateListings();
    }
    @Scheduled(cron = "0 0 23 * * ?") // Rulează zilnic la ora 23
    @Transactional
    public void updateListings() {
        log.info("Running cron job...  updating listings...");
        List<Listing> listings = listingRepository.findAll();
        Date currentDate = new Date();
        Date tomorrow = getNextDay();


        for (Listing listing : listings) {
            //check if listing should be deleted
            if (isSameDay(listing.getEndDate(), currentDate)) {
                listingRepository.delete(listing);
                continue;
            }
            ListingRentalDetails mostRecentRentalDetails = listing.getMostRecentRentalDetails();

            if (mostRecentRentalDetails != null) {
                // tomorrow is the first day to park
                if (isSameDay(mostRecentRentalDetails.getStartDate(),tomorrow)) {
                    SetCurrentUserDetailsAndSendAdminMessages(listing, mostRecentRentalDetails, mostRecentRentalDetails);
                }
                // today was the last day of parking
                if (isSameDay(mostRecentRentalDetails.getEndDate(),currentDate)) {
                    listing.markListingRentalDetailsInactive(mostRecentRentalDetails);
                    chatService.sendAdminMessage(
                            mostRecentRentalDetails.getUser().getUserUUID(),
                            String.format(ApplicationConstants.GENERIC_STOP_PARKING_MESSAGE,mostRecentRentalDetails.getListing().getTitle())
                    );
                    chatService.sendAdminMessage(
                            mostRecentRentalDetails.getListing().getUser().getUserUUID(),
                            String.format(ApplicationConstants.GENERIC_PARKING_SPOT_FREED, mostRecentRentalDetails.getCarNumber())
                    );
                    // the next entry is the most recent rental now
                    ListingRentalDetails nextRentalDetails = listing.getMostRecentRentalDetails();
                    if (nextRentalDetails != null) {
                        if (isSameDay(nextRentalDetails.getStartDate(),tomorrow)) {
                            SetCurrentUserDetailsAndSendAdminMessages(listing, mostRecentRentalDetails, nextRentalDetails);
                        } else {
                            listing.setCurrentCarNumber(null);
                            listing.setAvailable(true);
                        }
                    } else {
                        listing.setCurrentCarNumber(null);
                        listing.setAvailable(true);
                    }
                }
            } else {
                listing.setCurrentCarNumber(null);
                listing.setAvailable(true);
            }
            listingRepository.save(listing);
        }
    }

    private void SetCurrentUserDetailsAndSendAdminMessages(Listing listing, ListingRentalDetails mostRecentRentalDetails, ListingRentalDetails nextRentalDetails) {
        listing.setCurrentCarNumber(nextRentalDetails.getCarNumber());
        listing.setAvailable(false);
        chatService.sendAdminMessage(mostRecentRentalDetails.getUser().getUserUUID(), ApplicationConstants.GENERIC_START_PARKING_MESSAGE);
        chatService.sendAdminMessage(mostRecentRentalDetails.getListing().getUser().getUserUUID(), String.format(ApplicationConstants.GENERIC_PARKING_SPOT_OCCUPIED,
                mostRecentRentalDetails.getCarNumber(),
                mostRecentRentalDetails.getListing().getTitle(),
                calculateDaysDifference(mostRecentRentalDetails.getStartDate(),mostRecentRentalDetails.getEndDate())));
    }

    private boolean isSameDay(Date date1, Date date2) {
        LocalDate localDate1 = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate2 = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate1.equals(localDate2);
    }
    public long calculateDaysDifference(Date startDate, Date endDate) {
        LocalDate startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endLocalDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return ChronoUnit.DAYS.between(startLocalDate, endLocalDate) + 1;
    }
    private Date getNextDay() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        return Date.from(tomorrow.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

}
