package unibuc.ro.ParkingApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import unibuc.ro.ParkingApp.model.listing.Listing;

import java.util.UUID;

@Repository
public interface ListingRepository extends JpaRepository<Listing, UUID> , ListingRepositoryCustom{
}
