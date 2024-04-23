package unibuc.ro.ParkingApp.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import unibuc.ro.ParkingApp.exception.ListingNotFound;
import unibuc.ro.ParkingApp.model.listing.Listing;
import unibuc.ro.ParkingApp.model.listing.ListingRequest;
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
    public List<Listing> getAllListings(){
        List<Listing> listingsFromDB = repository.findAll();
        return listingsFromDB.stream().toList();
    }
    public Listing createListing(ListingRequest listingRequest){
        Listing listing = listingMapper.listingRequestToListing(listingRequest);
        User publishingUser = userService.getUserById(listingRequest.getUserUUID());
        listing.setUser(publishingUser);
        listing.setPublishingDate(LocalDateTime.now());
        repository.save(listing);
        return listing;
    }
    private Listing tryToGetListing(UUID uuid){
        Optional<Listing> listingsFromDB = repository.findById(uuid);
        if (listingsFromDB.isEmpty()){
            throw new ListingNotFound(uuid.toString());
        }
        return listingsFromDB.get();
    }

}
