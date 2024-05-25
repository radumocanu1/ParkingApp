package unibuc.ro.ParkingApp.controller;

import jakarta.ws.rs.PUT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import unibuc.ro.ParkingApp.configuration.ApplicationConstants;
import unibuc.ro.ParkingApp.model.listing.*;
import unibuc.ro.ParkingApp.model.PictureType;
import unibuc.ro.ParkingApp.service.ListingService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/listing")
public class ListingController {
    @Autowired
    ListingService listingService;
    @PostMapping()
    public ResponseEntity<Listing> createListing(@Validated @RequestBody ListingRequest listingRequest,  Principal principal){
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        return new ResponseEntity<>(listingService.createListing(listingRequest, (String) token.getTokenAttributes().get("sub")), HttpStatus.OK);

    }
    @GetMapping()
    public ResponseEntity<List<MinimalListing>> getAllListings(){
        return new ResponseEntity<>(listingService.getAllListings(), HttpStatus.OK);
    }
    @PostMapping("/filter")
    public ResponseEntity<List<MinimalListing>> getAllListingsByFilter(@RequestBody AdvanceFilteringRequest advanceFilteringRequest){
        return new ResponseEntity<>(listingService.getFilteredListings(advanceFilteringRequest),HttpStatus.OK);
    }

    @GetMapping("/{listingUUID}")
    public ResponseEntity<ListingResponse> getListing(@PathVariable String listingUUID, Principal principal){
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        return new ResponseEntity<>(listingService.getListingResponse((String) token.getTokenAttributes().get("sub"),UUID.fromString(listingUUID)), HttpStatus.OK);
    }
    @GetMapping("/myListings")
    public ResponseEntity<List<MinimalListing>> getCurrentUserListings(Principal principal){
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        return new ResponseEntity<>(listingService.getCurrentUserMinimalListings((String) token.getTokenAttributes().get("sub")), HttpStatus.OK);
    }
    @GetMapping("/userListings/{userUUID}")
    public ResponseEntity<List<MinimalListing>> getUserListings(@PathVariable String userUUID){
        return new ResponseEntity<>(listingService.getUserMinimalListings(UUID.fromString(userUUID)), HttpStatus.OK);
    }
    @PutMapping("/{listingUUID}")
    public ResponseEntity<Listing> updateListing(@PathVariable String listingUUID, @RequestBody ListingRequest listingRequest){
        return new ResponseEntity<>(listingService.updateListing(listingRequest, UUID.fromString(listingUUID)),  HttpStatus.OK);
    }
    @PatchMapping("/picture/{listingUUID}")
    public ResponseEntity<String> addPictureToListing(@PathVariable String listingUUID, @RequestBody MultipartFile file){
        listingService.addPhotoToListing(UUID.fromString(listingUUID), file, PictureType.REGULAR_PICTURE);
        return new ResponseEntity<>(String.format(ApplicationConstants.LISTING_PHOTO_ADDED, listingUUID), HttpStatus.OK);
    }
    @PatchMapping("/main-picture/{listingUUID}")
    public ResponseEntity<String> addMainPictureToListing(@PathVariable String listingUUID, @RequestBody MultipartFile file){
        listingService.addPhotoToListing(UUID.fromString(listingUUID), file, PictureType.MAIN_PICTURE);
        return new ResponseEntity<>(String.format(ApplicationConstants.LISTING_PHOTO_ADDED, listingUUID), HttpStatus.OK);
    }
    @GetMapping("/admin")
    public ResponseEntity<List<MinimalListing>> getAdminListings(){
        return new ResponseEntity<>(listingService.getAdminMinimalListings(), HttpStatus.OK);
    }
    @PutMapping("/admin/{listingUUID}")
    public ResponseEntity<Void> updateListingStatus(@PathVariable String listingUUID,@RequestBody AdminUpdateListingStatusRequest adminUpdateListingStatusRequest){
        listingService.updateListingStatusAdmin(UUID.fromString(listingUUID),adminUpdateListingStatusRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @DeleteMapping("/{listingUUID}")
    public ResponseEntity<String> deleteListing(@PathVariable String listingUUID){
        listingService.deleteListing(UUID.fromString(listingUUID));
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/{listingUUID}/unavailable-dates")
    public List<DateRange> getUnavailableDates(@PathVariable UUID listingUUID) {
        return listingService.getUnavailableDates(listingUUID);
    }
    @GetMapping("/rented")
    public ResponseEntity<List<MinimalListing>> getRentedListings(Principal principal){
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        return new ResponseEntity<>(listingService.getUserRentedListings((String) token.getTokenAttributes().get("sub")), HttpStatus.OK);
    }


}
