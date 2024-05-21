package unibuc.ro.ParkingApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import unibuc.ro.ParkingApp.model.user.UserChatMapping;

import java.util.UUID;

@RequestMapping
public interface UserChatsMappingRepository extends JpaRepository<UserChatMapping, UUID> {
}
