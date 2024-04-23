package unibuc.ro.ParkingApp.service.mapper;

import org.mapstruct.Mapper;
import unibuc.ro.ParkingApp.model.listing.Listing;
import unibuc.ro.ParkingApp.model.listing.ListingRequest;

@Mapper(componentModel = "spring")

public interface ListingMapper {
    Listing listingRequestToListing(ListingRequest listingRequest);

}
