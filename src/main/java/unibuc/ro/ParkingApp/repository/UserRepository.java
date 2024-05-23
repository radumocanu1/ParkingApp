package unibuc.ro.ParkingApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unibuc.ro.ParkingApp.model.user.User;
import unibuc.ro.ParkingApp.model.user.UserProfilePictureDTO;

import java.util.UUID;

@Repository
public interface UserRepository  extends JpaRepository<User, UUID> {
    @Query("SELECT new unibuc.ro.ParkingApp.model.user.UserProfilePictureDTO(u.profilePicturePath, u.hasProfilePicture) FROM User u WHERE u.userUUID = :userUUID")
    UserProfilePictureDTO findProfilePictureInfoByUserUUID(@Param("userUUID") UUID userUUID);
}