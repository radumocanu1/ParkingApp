package unibuc.ro.ParkingApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import unibuc.ro.ParkingApp.model.PendingPayment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PendingPaymentsRepository extends JpaRepository<PendingPayment, UUID> {
    @Transactional
    void deleteByUserUUID(UUID userUUID);
    @Transactional
    void deleteAllByUserUUID(UUID userUUID);
    Optional<PendingPayment> findByUserUUID(UUID userUUID);
    List<PendingPayment> findAllByUserUUID(UUID userUUID);


}
