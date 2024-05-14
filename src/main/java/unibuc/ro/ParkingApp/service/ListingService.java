package unibuc.ro.ParkingApp.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import unibuc.ro.ParkingApp.exception.ListingNotFound;
import unibuc.ro.ParkingApp.model.listing.Listing;
import unibuc.ro.ParkingApp.model.listing.ListingRequest;
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
        Listing listing = tryToGetListing(listingUUID);
        listingMapper.fill(listingRequest,listing);
        // pictures should be deleted at this step
        repository.save(listing);
        return listing;
    }


    public Listing getListing(UUID listingUUID){
        return tryToGetListing(listingUUID);
    }
    private Listing tryToGetListing(UUID uuid){
        Optional<Listing> listingsFromDB = repository.findById(uuid);
        if (listingsFromDB.isEmpty()){
            throw new ListingNotFound(uuid.toString());
        }
        return listingsFromDB.get();
    }
    private List<byte[]> extractBytesFromListingPictures(List<MultipartFile> multipartFiles){
        List<byte[]> picturesBytesList = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles){
            picturesBytesList.add(fileService.extractFileBytes(multipartFile));
        }
        return picturesBytesList;
    }


}
