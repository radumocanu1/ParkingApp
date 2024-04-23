package unibuc.ro.ParkingApp.model.listing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import unibuc.ro.ParkingApp.model.picture.Picture;
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
    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL)
    private  List<Picture> pictures;

    private String title;
    String latitude;
    String longitude;
    Date startDate;
    Date endDate;
    int parkingSpotSlotNumber;
    @JsonFormat(with = JsonFormat.Feature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE,  shape = JsonFormat.Shape.STRING, pattern = "MM-dd HH:mm")
    LocalDateTime publishingDate;


    // this will always be the "per/day" price
    int price;



}
