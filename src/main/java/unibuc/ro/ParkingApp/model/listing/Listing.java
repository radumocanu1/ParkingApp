package unibuc.ro.ParkingApp.model.listing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import unibuc.ro.ParkingApp.model.PictureType;
import unibuc.ro.ParkingApp.model.user.User;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "listing")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID listingUUID;
    @ManyToOne
    @JoinColumn(name = "userUUID", nullable = false)
    User user;
    private String mainPicture;
    private String title;
    String latitude;
    String longitude;
    Date startDate;
    Date endDate;
    int parkingSpotSlotNumber;
    int sector;
    String location;
    String description;
    boolean longTermRent;
    int monthlyPrice;
    @JsonFormat(with = JsonFormat.Feature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE,  shape = JsonFormat.Shape.STRING, pattern = "MM-dd HH:mm")
    LocalDateTime publishingDate;
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "pictures", joinColumns = @JoinColumn(name = "listingUUID"))
    @Column(name = "picture")
    private List<String> pictures;
    @ElementCollection(targetClass = Date.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "unavailable_dates", joinColumns = @JoinColumn(name = "listingUUID"))
    @Column(name = "unavailable_dates")
    private List<Date> unavailableDates;
    int price;
    int rating;
    boolean available;
    Status status;

    public void addPicture(String picture, PictureType pictureType) {
        if (pictureType == PictureType.MAIN_PICTURE)
            this.mainPicture = picture;
        else
            this.pictures.add(picture);
    }



}
