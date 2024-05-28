package unibuc.ro.ParkingApp.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import unibuc.ro.ParkingApp.model.listing.*;

import java.util.List;

@Mapper(componentModel = "spring")

public interface ListingMapper {
    @Mapping(target = "pictures", ignore = true)
    Listing listingRequestToListing(ListingRequest listingRequest);
    @Mapping(target = "mainPicture", ignore = true)
    MinimalListing listingToMinimalListing(Listing listing);
    @Mapping(target = "mainPicture", ignore = true)
    @Mapping(target = "pictures", ignore = true)
    ListingResponse listingToListingResponse(Listing listing);
    @Mapping(target = "mainPicture", ignore = true)
    MapsListing listingToMapsListing(Listing listing);
    void fill(ListingRequest listingRequest, @MappingTarget Listing listing);


}
