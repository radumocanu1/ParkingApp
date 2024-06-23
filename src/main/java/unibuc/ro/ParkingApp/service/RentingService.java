package unibuc.ro.ParkingApp.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unibuc.ro.ParkingApp.model.listing.Listing;
import unibuc.ro.ParkingApp.model.listing.ListingRentalDetails;
import unibuc.ro.ParkingApp.model.listing.ListingRentalDetailsKey;
import unibuc.ro.ParkingApp.model.user.User;
import unibuc.ro.ParkingApp.repository.ListingRentalDetailsRepository;

import java.util.Date;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RentingService {
    private final ListingRentalDetailsRepository listingRentalDetailsRepository;
    private final UserService userService;
    private final ListingService listingService;
    @Transactional
    public void rent(UUID userUUID, UUID listingUUID, Date startDate, Date endDate, String carNumber) {
        ListingRentalDetailsKey rentalDetailsKey = new ListingRentalDetailsKey(userUUID, listingUUID);
        ListingRentalDetails rentalDetails = new ListingRentalDetails();
        rentalDetails.setListingRentalDetailsKey(rentalDetailsKey); // Set the composite key
        rentalDetails.setActive(true);
        rentalDetails.setUser(userService.getUserById(userUUID));
        rentalDetails.setListing(listingService.getListing(listingUUID));
        rentalDetails.setCarNumber(carNumber);
        rentalDetails.setStartDate(startDate);
        rentalDetails.setEndDate(endDate);

        listingRentalDetailsRepository.save(rentalDetails);
    }
}
