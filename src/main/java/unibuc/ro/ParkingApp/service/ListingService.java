package unibuc.ro.ParkingApp.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import unibuc.ro.ParkingApp.exception.ListingNotFound;
import unibuc.ro.ParkingApp.model.listing.Listing;
import unibuc.ro.ParkingApp.model.listing.ListingRequest;
import unibuc.ro.ParkingApp.model.picture.Picture;
import unibuc.ro.ParkingApp.model.user.User;
import unibuc.ro.ParkingApp.repository.ListingRepository;
import unibuc.ro.ParkingApp.service.mapper.ListingMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor

public class ListingService {
    ListingRepository repository;
    ListingMapper listingMapper;
    UserService userService;
    PictureService pictureService;
    public List<Listing> getAllListings(){
        List<Listing> listingsFromDB = repository.findAll();
        return listingsFromDB.stream().toList();
    }
    public Listing createListing(ListingRequest listingRequest, UUID userUUID){
        Listing listing = listingMapper.listingRequestToListing(listingRequest);
        User publishingUser = userService.getUserById(userUUID);
        listing.setUser(publishingUser);
        listing.setPublishingDate(LocalDateTime.now());
        repository.save(listing);
        addPicturesToDB(listingRequest.getPictures(),  listing);
        return listing;
    }
    public Listing updateListing(ListingRequest listingRequest, UUID listingUUID){
        Listing listing = tryToGetListing(listingUUID);
        listingMapper.fill(listingRequest,listing);
        // pictures should be deleted at this step
        repository.save(listing);
        addPicturesToDB(listingRequest.getPictures(), listing);
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

    private void addPicturesToDB(List<Picture> pictures,Listing listing){
        if (pictures != null){
            for (Picture picture:pictures){
                pictureService.addPicture(picture, listing);
            }
        }

    }


}
