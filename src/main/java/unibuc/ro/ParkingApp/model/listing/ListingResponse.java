package unibuc.ro.ParkingApp.model.listing;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import unibuc.ro.ParkingApp.model.user.MinimalUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ListingResponse {
    UUID listingUUID;
    MinimalUser minimalUser;
    byte[] mainPicture;
    List<byte[]> pictures = new ArrayList<>();
    String title;
    String latitude;
    String longitude;
    Date startDate;
    Date endDate;
    int parkingSpotSlotNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    LocalDateTime publishingDate;
    int price;
    int sector;
    String location;
    boolean myListing;
    String description;
    boolean longTermRent;
    double rating;
    int monthlyPrice;
    Status status;
    String currentCarNumber;

    public void addPicture(byte[] picture) {
        this.pictures.add(picture);
    }


}
