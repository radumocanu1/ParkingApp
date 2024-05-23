package unibuc.ro.ParkingApp.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import unibuc.ro.ParkingApp.model.listing.AdvanceFilteringRequest;
import unibuc.ro.ParkingApp.model.listing.Listing;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ListingRepositoryImpl implements ListingRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Listing> findByAdvanceFiltering(AdvanceFilteringRequest request) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Listing> cq = cb.createQuery(Listing.class);
        Root<Listing> listing = cq.from(Listing.class);

        List<Predicate> predicates = new ArrayList<>();

        if (request.getSector() != 0) {
            predicates.add(cb.equal(listing.get("sector"), request.getSector()));
        }

        if (request.getStartDate() != null) {
            predicates.add(cb.greaterThanOrEqualTo(listing.get("startDate"), request.getStartDate()));
        }

        if (request.getEndDate() != null) {
            predicates.add(cb.lessThanOrEqualTo(listing.get("endDate"), request.getEndDate()));
        }

        if (request.getMaxDailyPrice() != 0) {
            predicates.add(cb.lessThanOrEqualTo(listing.get("price"), request.getMaxDailyPrice()));
        }

        if (request.getMaxMonthlyPrice() != 0) {
            predicates.add(cb.lessThanOrEqualTo(listing.get("monthlyPrice"), request.getMaxMonthlyPrice()));
        }

        if (request.isIndefinitePeriod()) {
            predicates.add(cb.isTrue(listing.get("longTermRent")));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(cq).getResultList();
    }
}