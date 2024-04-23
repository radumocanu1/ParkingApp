package unibuc.ro.ParkingApp.model.feedback;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
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
    @JoinColumn(name = "userUUID", nullable = false)
    @JsonIgnore
    private User user;
    private UUID feedbackAuthor ;
    private String message;
    private int ratingGiven;
    private boolean isAnonymous;
}