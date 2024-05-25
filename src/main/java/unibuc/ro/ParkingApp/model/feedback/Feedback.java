package unibuc.ro.ParkingApp.model.feedback;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import unibuc.ro.ParkingApp.model.listing.Listing;

import java.time.LocalDateTime;
import java.util.Date;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    LocalDateTime publishingDate;
    private String message;
    private int ratingGiven;
    private UUID authorUUID;

}