package unibuc.ro.ParkingApp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "feedback")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID feedbackUUID;
    @ManyToOne
    @JoinColumn(name = "userUUID", nullable = false)
    private User user;
    private UUID feedbackAuthor ;
    private String message;
    private int ratingGiven;

}