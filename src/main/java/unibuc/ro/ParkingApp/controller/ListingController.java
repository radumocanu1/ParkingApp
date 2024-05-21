package unibuc.ro.ParkingApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import unibuc.ro.ParkingApp.configuration.ApplicationConstants;
import unibuc.ro.ParkingApp.model.listing.Listing;
import unibuc.ro.ParkingApp.model.listing.ListingRequest;
import unibuc.ro.ParkingApp.model.PictureType;
import unibuc.ro.ParkingApp.model.listing.ListingResponse;
import unibuc.ro.ParkingApp.model.listing.MinimalListing;
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

    @GetMapping("/{listingUUID}")
    public ResponseEntity<ListingResponse> getListing(@PathVariable String listingUUID){
        return new ResponseEntity<>(listingService.getListingResponse(UUID.fromString(listingUUID)), HttpStatus.OK);
    }
    @GetMapping("/userListings")
    public ResponseEntity<List<MinimalListing>> getUserListings(Principal principal){
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        return new ResponseEntity<>(listingService.getUserMinimalListings((String) token.getTokenAttributes().get("sub")), HttpStatus.OK);
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
    @DeleteMapping("/{listingUUID}")
    public ResponseEntity<String> deleteListing(@PathVariable String listingUUID){
        listingService.deleteListing(UUID.fromString(listingUUID));
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
