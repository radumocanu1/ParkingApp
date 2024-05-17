package unibuc.ro.ParkingApp.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import unibuc.ro.ParkingApp.exception.ListingNotFound;
import unibuc.ro.ParkingApp.model.listing.Listing;
import unibuc.ro.ParkingApp.model.listing.ListingRequest;
import unibuc.ro.ParkingApp.model.PictureType;
import unibuc.ro.ParkingApp.model.listing.ListingResponse;
import unibuc.ro.ParkingApp.model.listing.MinimalListing;
import unibuc.ro.ParkingApp.model.user.User;
import unibuc.ro.ParkingApp.repository.ListingRepository;
import unibuc.ro.ParkingApp.service.mapper.ListingMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor

public class ListingService {
    private static final Logger log = LoggerFactory.getLogger(ListingService.class);
    ListingRepository repository;
    ListingMapper listingMapper;
    UserService userService;
    OIDCUserMappingService oidcUserMappingService;
    FileService fileService;

    public List<Listing> getAllListings(){
        List<Listing> listingsFromDB = repository.findAll();
        return listingsFromDB.stream().toList();
    }
    public List<ListingResponse> getAllListingsResponses(){
        List<Listing> listingsFromDB = repository.findAll();
        List<ListingResponse> listingResponses = new ArrayList<>();
        for (Listing listing : listingsFromDB) {
            listingResponses.add(getListingResponse(listing.getListingUUID()));
        }
        return listingResponses;
    }
//    public List<MinimalListing> getAllMinimalListings(){
//
//    }
    public ListingResponse getListingResponse(UUID listingId){
        Listing listing = getListing(listingId);
        ListingResponse listingResponse = listingMapper.listingToListingResponse(listing);
        listingResponse.setMainPicture(fileService.loadPicture(listing.getMainPicture()));
        addPicturesToListingResponse(listingResponse,listing.getPictures());
        return listingResponse;
    }
    public Listing createListing(ListingRequest listingRequest, String tokenSubClaim){
        log.info("createListing");
        Listing listing = listingMapper.listingRequestToListing(listingRequest);
        User publishingUser = oidcUserMappingService.findBySubClaim(tokenSubClaim).getUser();
        listing.setUser(publishingUser);
        listing.setPublishingDate(LocalDateTime.now());
        repository.save(listing);
        log.info("createListing successful");
        return listing;
    }
    public Listing updateListing(ListingRequest listingRequest, UUID listingUUID){
        Listing listing = getListing(listingUUID);
        listingMapper.fill(listingRequest,listing);
        // pictures should be deleted at this step
        repository.save(listing);
        return listing;
    }
    public synchronized void addPhotoToListing(UUID listingUUID, MultipartFile file, PictureType pictureType){
        log.info("Adding " + file.getOriginalFilename() + " to listing "  + listingUUID + " ..." );
        Listing listing = getListing(listingUUID);
        listing.addPicture(fileService.saveFile(listingUUID,file), pictureType);
        repository.save(listing);
        log.info("Photo successfully added!" );

    }


    public Listing getListing(UUID uuid){
        Optional<Listing> listingsFromDB = repository.findById(uuid);
        if (listingsFromDB.isEmpty()){
            throw new ListingNotFound(uuid.toString());
        }
        return listingsFromDB.get();
    }

    private void addPicturesToListingResponse(ListingResponse listingResponse, List<String> pictures){
        for (String picture : pictures){
            listingResponse.addPicture(fileService.loadPicture(picture));
        }
    }


}
