package unibuc.ro.ParkingApp.model.listing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MapsListing {
    UUID listingUUID;
    String latitude;
    String longitude;
    byte[] mainPicture;
    String title;
    int price;
    boolean longTermRent;
    int monthlyPrice;

}
