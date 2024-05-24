package unibuc.ro.ParkingApp.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unibuc.ro.ParkingApp.model.listing.Listing;

import java.util.UUID;

@Repository
public interface ListingRepository extends JpaRepository<Listing, UUID> , ListingRepositoryCustom{
}
