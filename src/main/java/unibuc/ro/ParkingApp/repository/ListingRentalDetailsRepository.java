package unibuc.ro.ParkingApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unibuc.ro.ParkingApp.model.listing.ListingRentalDetails;

public interface ListingRentalDetailsRepository extends JpaRepository<ListingRentalDetails, Long> {
}
