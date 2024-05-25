package unibuc.ro.ParkingApp.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import unibuc.ro.ParkingApp.exception.FileNotDeleted;
import unibuc.ro.ParkingApp.exception.ListingNotFound;
import unibuc.ro.ParkingApp.model.chat.MinimalChat;
import unibuc.ro.ParkingApp.model.listing.*;
import unibuc.ro.ParkingApp.model.PictureType;
import unibuc.ro.ParkingApp.model.user.User;
import unibuc.ro.ParkingApp.repository.ListingRepository;
import unibuc.ro.ParkingApp.service.mapper.ListingMapper;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor

public class ListingService {
    private static final Logger log = LoggerFactory.getLogger(ListingService.class);
    private final ListingRepository listingRepository;
    ListingMapper listingMapper;
    UserService userService;
    OIDCUserMappingService oidcUserMappingService;
    FileService fileService;
    ChatService chatService;

    public List<MinimalListing> getAllListings(){
        log.info("Getting all listings");
        List<Listing> listingsFromDB = listingRepository.findAll();
        return convertListingsToMinimalListings(listingsFromDB.stream().filter(
                (listing -> !listing.isLongTermRent() && listing.getStatus()!= Status.PENDING && listing.getStatus()!= Status.DEACTIVATED))
                .toList());

    }
    public List<MinimalListing> getFilteredListings(AdvanceFilteringRequest advanceFilteringRequest){
        log.info("Getting filtered listings");
        log.info("AdvanceFilteringRequest: {}", advanceFilteringRequest);
        List<Listing> filteredListings = listingRepository.findByAdvanceFiltering(advanceFilteringRequest);
        return convertListingsToMinimalListings(filteredListings);
    }

    public ListingResponse getListingResponse(String tokenSubClaim, UUID listingId){
        Listing listing = getListing(listingId);
        UUID currentUserUUID = oidcUserMappingService.findBySubClaim(tokenSubClaim).getUser().getUserUUID();
        ListingResponse listingResponse = listingMapper.listingToListingResponse(listing);
        listingResponse.setMainPicture(fileService.loadPicture(listing.getMainPicture()));
        addPicturesToListingResponse(listingResponse,listing.getPictures());
        listingResponse.setMinimalUser(userService.createMinimalUser(listing.getUser()));
        listingResponse.setMyListing(currentUserUUID == listing.getUser().getUserUUID());
        return listingResponse;
    }
    public Listing createListing(ListingRequest listingRequest, String tokenSubClaim){
        log.info("createListing");
        System.out.println(listingRequest);
        Listing listing = listingMapper.listingRequestToListing(listingRequest);
        User publishingUser = oidcUserMappingService.findBySubClaim(tokenSubClaim).getUser();
        listing.setUser(publishingUser);
        listing.setPublishingDate(LocalDateTime.now());
        listing.setStatus(Status.PENDING);
        listingRepository.save(listing);
        log.info("createListing successful");
        return listing;
    }
    public List<MinimalListing> getUserRentedListings(String tokenSubClaim){
        User user = oidcUserMappingService.findBySubClaim(tokenSubClaim).getUser();
        List<ListingRentalDetails>  listingRentalDetails= user.getListingRentalDetails();
        return convertListingRentalDetailsToMinimalListings(listingRentalDetails);

    }
    public List<DateRange> getUnavailableDates(UUID listingUUID) {
        Listing listing = listingRepository.findById(listingUUID).orElse(null);
        if (listing == null) {
            throw new RuntimeException("Listing not found");
        }

        return listing.getListingRentalDetails().stream()
                .map(details -> new DateRange(details.getStartDate(), details.getEndDate()))
                .collect(Collectors.toList());
    }
    public Listing updateListing(ListingRequest listingRequest, UUID listingUUID){
        Listing listing = getListing(listingUUID);
        listingMapper.fill(listingRequest,listing);
        // todo maybe find a way to do this more optimal ( not just delete all pictures and them add them back)
        deleteListingPictures(listing);
        listing.setPictures(new ArrayList<>());
        listing.setMainPicture(null);
        listingRepository.save(listing);
        return listing;
    }
    public void deleteListing(UUID listingUUID){
        log.info("deleting listing with UUID {} ...", listingUUID);
        // todo check if request was done by publishing user or admin
        Listing listing = getListing(listingUUID);
        fileService.deleteDirectory(listingUUID);
        listingRepository.delete(listing);
        log.info("successfully deleted listing!");
    }
    public synchronized void addPhotoToListing(UUID listingUUID, MultipartFile file, PictureType pictureType){
        log.info("Adding " + file.getOriginalFilename() + " to listing "  + listingUUID + " ..." );
        Listing listing = getListing(listingUUID);
        listing.addPicture(fileService.saveFile(listingUUID.toString(),file), pictureType);
        listingRepository.save(listing);
        log.info("Photo successfully added!" );

    }

    public void saveListing(Listing listing){
        listingRepository.save(listing);
    }
    public Listing getListing(UUID uuid){
        Optional<Listing> listingsFromDB = listingRepository.findById(uuid);
        if (listingsFromDB.isEmpty()){
            throw new ListingNotFound(uuid.toString());
        }
        return listingsFromDB.get();
    }
    public List<MinimalListing> getCurrentUserMinimalListings(String tokenSubClaim){
        List<Listing> listings = userService.getUserListings(tokenSubClaim);

        return convertListingsToMinimalListings(listings);
    }
    public List<MinimalListing> getUserMinimalListings(UUID userUUID){
        User user = userService.getUserById(userUUID);
        return convertListingsToMinimalListings(user.getListings().stream()
                .filter((listing -> listing.getStatus()!= Status.PENDING)).toList());
    }
    public List<MinimalListing> getAdminMinimalListings(){
        List<Listing> listings = listingRepository.findAll();
        return convertListingsToMinimalListings(listings.stream()
                .filter((listing -> listing.getStatus()== Status.PENDING)).toList());
    }
    public void updateListingStatusAdmin(UUID listingUUID, AdminUpdateListingStatusRequest adminUpdateListingStatusRequest){
        Listing listing = getListing(listingUUID);
        UUID userUUID = listing.getUser().getUserUUID();
        if (adminUpdateListingStatusRequest.getAdminListingDecision() == AdminListingDecision.ACCEPT){
            listing.setStatus(Status.ACTIVE);
            listingRepository.save(listing);
        }
        else {
            listingRepository.delete(listing);
        }
        chatService.sendAdminMessage(userUUID,adminUpdateListingStatusRequest.getMessage());


    }
    private List<MinimalListing> convertListingRentalDetailsToMinimalListings(List<ListingRentalDetails> listingRentalDetails){
        List<Listing> listings = new ArrayList<>();
        for (ListingRentalDetails listingRentalDetail : listingRentalDetails){
            Listing listing = listingRentalDetail.getListing();
            listing.setStartDate(listingRentalDetail.getStartDate());
            listing.setEndDate(listingRentalDetail.getEndDate());
            listings.add(listing);
        }
        return convertListingsToMinimalListings(listings);
    }

    private void addPicturesToListingResponse(ListingResponse listingResponse, List<String> pictures){
        for (String picture : pictures){
            listingResponse.addPicture(fileService.loadPicture(picture));
        }
    }
    private void deleteListingPictures(Listing listing){
        try {
            fileService.deleteFile(listing.getMainPicture());
            for (String picture : listing.getPictures()){
                fileService.deleteFile(picture);
            }
        }catch(FileNotDeleted e){
            log.error(e.toString());
        }
    }

    private List<MinimalListing> convertListingsToMinimalListings(List<Listing> listings){
        log.info("Converting Listings into MinimalListings");
        List<MinimalListing> minimalListings = new ArrayList<>();
        for (Listing listing : listings) {
            byte[] mainPictureBytes = fileService.loadPicture(listing.getMainPicture());
            MinimalListing minimalListing = listingMapper.listingToMinimalListing(listing);
            minimalListing.setMainPicture(mainPictureBytes);
            minimalListings.add(minimalListing);
        }
        log.info("returning minimalListings");
        return minimalListings;
    }

}
