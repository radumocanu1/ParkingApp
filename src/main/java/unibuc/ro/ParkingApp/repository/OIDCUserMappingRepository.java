package unibuc.ro.ParkingApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unibuc.ro.ParkingApp.model.user.OIDCUserMapping;


public interface OIDCUserMappingRepository extends JpaRepository<OIDCUserMapping, String> {
}
