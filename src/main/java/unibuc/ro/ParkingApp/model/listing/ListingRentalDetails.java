package unibuc.ro.ParkingApp.model.listing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import unibuc.ro.ParkingApp.model.user.User;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "listing_rental_details")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ListingRentalDetails {
    @EmbeddedId
    ListingRentalDetailsKey listingRentalDetailsKey;
    @ManyToOne
    @MapsId("listingUUID")
    @JoinColumn(name = "listingUUID")
    @JsonIgnore
    Listing listing;
    @ManyToOne
    @MapsId("userUUID")
    @JoinColumn(name = "userUUID")
    @JsonIgnore

    User user;
    String carNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    Date startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    Date endDate;
    boolean active;



}
