package unibuc.ro.ParkingApp.model.listing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MinimalListing {
    String title;
    byte[] mainPicture;
    int sector;
    UUID listingUUID;
    Date startDate;
    Date endDate;
    LocalDateTime publishingDate;
    int price;
    int rating;
    boolean available;
    Status status;

}
