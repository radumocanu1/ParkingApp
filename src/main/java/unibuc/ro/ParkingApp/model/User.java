package unibuc.ro.ParkingApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import unibuc.ro.ParkingApp.validator.UserEmailValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Log4j2
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID userUUID;
    @NotBlank(message = "Username is required")
    String username;
    @NotBlank(message = "Email is required")
    @UserEmailValidator
    String email;
    @NotBlank(message = "Password is required")
    String password;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feedback> feedbackList = new ArrayList<>();
    private boolean isTrusted;
    private double rating;
    private String profilePicturePath;

    public void computeNewRating() {
        this.rating = feedbackList.stream()
                .map(Feedback::getRatingGiven)
                .mapToInt(i -> i)
                .average()
                .orElse(0);

    }
}