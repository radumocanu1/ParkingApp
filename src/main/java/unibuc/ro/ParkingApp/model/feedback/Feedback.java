package unibuc.ro.ParkingApp.model.feedback;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import unibuc.ro.ParkingApp.model.listing.Listing;
import unibuc.ro.ParkingApp.model.user.User;

import java.util.UUID;

@Entity
@Table(name = "feedback")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID feedbackUUID;
    @ManyToOne
    @JoinColumn(name = "listingUUID", nullable = false)
    @JsonIgnore
    private Listing listing;
    private String message;
    private int ratingGiven;
    private UUID authorUUID;

}