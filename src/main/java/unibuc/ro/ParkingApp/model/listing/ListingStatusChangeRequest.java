package unibuc.ro.ParkingApp.model.listing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListingStatusChangeRequest {
    Status status;
}
