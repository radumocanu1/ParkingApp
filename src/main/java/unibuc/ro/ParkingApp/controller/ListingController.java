package unibuc.ro.ParkingApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import unibuc.ro.ParkingApp.model.listing.Listing;
import unibuc.ro.ParkingApp.model.listing.ListingRequest;
import unibuc.ro.ParkingApp.service.ListingService;

import java.util.List;

@Controller
@RequestMapping("/listing")
public class ListingController {
    @Autowired
    ListingService listingService;
    @PostMapping()
    public ResponseEntity<Listing> createUser(@Validated @RequestBody ListingRequest listingRequest){
        return new ResponseEntity<>(listingService.createListing(listingRequest), HttpStatus.OK);

    }
    @GetMapping()
    public ResponseEntity<List<Listing>> getAllUsers(){
        return new ResponseEntity<>(listingService.getAllListings(), HttpStatus.OK);
    }
}
