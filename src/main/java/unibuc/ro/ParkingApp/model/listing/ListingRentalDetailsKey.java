package unibuc.ro.ParkingApp.model.listing;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
public class ListingRentalDetailsKey {
    @Column(name = "userUUID")
    UUID userUUID;

    @Column(name = "listingUUID")
    UUID listingUUID;

}
