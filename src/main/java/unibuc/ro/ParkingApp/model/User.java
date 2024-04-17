package unibuc.ro.ParkingApp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import unibuc.ro.ParkingApp.validator.UserEmailValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
}
