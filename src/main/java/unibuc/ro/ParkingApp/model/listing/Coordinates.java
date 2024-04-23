package unibuc.ro.ParkingApp.model.listing;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Data
@Entity(name = "coordinates")
public class Coordinates {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID coordinatesUUID;
    String latitude;
    String longitude;
}
