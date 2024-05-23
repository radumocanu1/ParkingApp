package unibuc.ro.ParkingApp.repository;

import unibuc.ro.ParkingApp.model.listing.AdvanceFilteringRequest;
import unibuc.ro.ParkingApp.model.listing.Listing;

import java.util.List;

public interface ListingRepositoryCustom {
    List<Listing> findByAdvanceFiltering(AdvanceFilteringRequest request);
}
