package unibuc.ro.ParkingApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import unibuc.ro.ParkingApp.model.listing.Listing;
import unibuc.ro.ParkingApp.model.listing.ListingRequest;
import unibuc.ro.ParkingApp.service.ListingService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/listing")
public class ListingController {
    @Autowired
    ListingService listingService;
    @PostMapping("/{userUUID}")
    public ResponseEntity<Listing> createListing(@Validated @RequestBody ListingRequest listingRequest, @PathVariable UUID userUUID){
        return new ResponseEntity<>(listingService.createListing(listingRequest, userUUID), HttpStatus.OK);

    }
    @GetMapping()
    public ResponseEntity<List<Listing>> getAllListings(){
        return new ResponseEntity<>(listingService.getAllListings(), HttpStatus.OK);
    }
    @GetMapping("/{listingUUID}")
    public ResponseEntity<Listing> getListing(@PathVariable UUID listingUUID){
        return new ResponseEntity<>(listingService.getListing(listingUUID), HttpStatus.OK);
    }
    @PutMapping("/{listingUUID}")
    public ResponseEntity<Listing> updateListing(@PathVariable UUID listingUUID, @RequestBody ListingRequest listingRequest){
        return new ResponseEntity<>(listingService.updateListing(listingRequest, listingUUID),  HttpStatus.OK);
    }
}
