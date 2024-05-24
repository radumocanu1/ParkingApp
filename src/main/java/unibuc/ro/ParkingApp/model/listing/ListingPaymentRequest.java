package unibuc.ro.ParkingApp.model.listing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ListingPaymentRequest {
    UUID listingUUID;
    String title;
    int price;

}
