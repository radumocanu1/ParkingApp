package unibuc.ro.ParkingApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import unibuc.ro.ParkingApp.model.listing.Listing;

import java.util.UUID;

@Entity
@Table(name = "pictures")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID pictureUUID;
    @ManyToOne
    @JoinColumn(name = "listingUUID", nullable = false)
    @JsonIgnore
    Listing listing;
    String picturePath;
}
