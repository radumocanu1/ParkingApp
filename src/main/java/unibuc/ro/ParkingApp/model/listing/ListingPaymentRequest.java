package unibuc.ro.ParkingApp.model.listing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ListingPaymentRequest {
    String title;
    int price;
}
