package unibuc.ro.ParkingApp.model.listing;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import unibuc.ro.ParkingApp.model.picture.Picture;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListingRequest {
    @NotBlank
    private String title;
    @NotNull
    Date startDate;
    @NotNull
    Date endDate;
    private List<Picture> pictures;
    String latitude;
    String longitude;
    int parkingSpotSlotNumber;
    int price;

}
