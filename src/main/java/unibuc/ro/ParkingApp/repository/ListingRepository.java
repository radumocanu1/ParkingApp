package unibuc.ro.ParkingApp.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unibuc.ro.ParkingApp.model.listing.Listing;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ListingRepository extends JpaRepository<Listing, UUID> {
    @Query("SELECT l FROM Listing l WHERE \n" +
            "    (:sector IS NULL OR l.sector = :sector) \n" +
            "    AND (:startDate IS NULL OR :endDate IS NULL OR l.startDate >= :startDate AND (l.endDate IS NULL OR l.endDate <= :endDate))\n" +
            "    AND (:maxDailyPrice IS NULL OR l.price <= :maxDailyPrice)\n" +
            "    AND (:maxMonthlyPrice IS NULL OR l.monthlyPrice <= :maxMonthlyPrice)\n" +
            "    AND (:indefinitePeriod = false OR (l.longTermRent = true AND (:sector IS NULL OR l.sector = :sector)))\n   ")
    List<Listing> findListingsByFilters(@Param("sector") Integer sector,
                                        @Param("startDate") Date startDate,
                                        @Param("endDate") Date endDate,
                                        @Param("maxDailyPrice") Integer maxDailyPrice,
                                        @Param("maxMonthlyPrice") Integer maxMonthlyPrice,
                                        @Param("indefinitePeriod") boolean indefinitePeriod);

}
