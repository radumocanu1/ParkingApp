package unibuc.ro.ParkingApp.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import unibuc.ro.ParkingApp.model.listing.Listing;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ListingRepository extends JpaRepository<Listing, UUID> {
//    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
//    Optional<Listing> findById(UUID listingId);

}
