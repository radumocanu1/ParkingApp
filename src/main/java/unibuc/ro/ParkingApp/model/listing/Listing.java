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
    @JsonIgnore
    User user;
    private String mainPicture;
    private String title;
    String latitude;
    String longitude;
    Date startDate;
    Date endDate;
    int parkingSpotSlotNumber;
    @JsonFormat(with = JsonFormat.Feature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE,  shape = JsonFormat.Shape.STRING, pattern = "MM-dd HH:mm")
    LocalDateTime publishingDate;
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "pictures", joinColumns = @JoinColumn(name = "listingUUID"))
    @Column(name = "picture", nullable = false)
    private List<String> pictures;

    // this will always be the "per/day" price
    int price;

    public void addPicture(String picture, PictureType pictureType) {
        if (pictureType == PictureType.MAIN_PICTURE)
            this.mainPicture = picture;
        else
            this.pictures.add(picture);
    }



}
