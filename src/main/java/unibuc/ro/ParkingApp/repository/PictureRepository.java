package unibuc.ro.ParkingApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unibuc.ro.ParkingApp.model.picture.Picture;

import java.util.UUID;
@Repository
public interface PictureRepository extends JpaRepository<Picture, UUID> {
}
