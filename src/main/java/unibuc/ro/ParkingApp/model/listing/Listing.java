package unibuc.ro.ParkingApp.model.listing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import unibuc.ro.ParkingApp.model.Picture;
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
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID listingUUID;
    @JsonFormat(with = JsonFormat.Feature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE,  shape = JsonFormat.Shape.STRING, pattern = "MM-dd HH:mm")
    @OneToOne
    Coordinates coordinates;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "userUUID", nullable = false)
    User user;
    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL, orphanRemoval = true)
    private  List<Picture> picturePaths;

    Date startDate;
    Date endDate;
    int parkingSpotSlotNumber;
    @JsonFormat(with = JsonFormat.Feature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE,  shape = JsonFormat.Shape.STRING, pattern = "MM-dd HH:mm")
    LocalDateTime publishingDate;
    private String title;

    // this will always be the "perday" price
    int price;



}
