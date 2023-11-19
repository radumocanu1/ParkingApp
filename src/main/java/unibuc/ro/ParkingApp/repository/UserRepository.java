package unibuc.ro.ParkingApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unibuc.ro.ParkingApp.model.User;

import java.util.UUID;

@Repository
public interface UserRepository  extends JpaRepository<User, UUID> {

}
