package unibuc.ro.ParkingApp.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import unibuc.ro.ParkingApp.model.feedback.Feedback;
import unibuc.ro.ParkingApp.model.listing.Listing;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID userUUID;
    String username;
    String email;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Listing> listings = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feedback> feedbackList = new ArrayList<>();
    private boolean isTrusted;
    private double rating;
    private boolean hasProfilePicture ;
    @Column(name = "profile_picture_bytes", columnDefinition = "LONGBLOB")
    private byte[] profilePictureBytes;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String sex;
    private int age;



    public void computeNewRating() {
        double averageRating = feedbackList.stream()
                .mapToInt(Feedback::getRatingGiven)
                .average()
                .orElse(0);
        DecimalFormat df = new DecimalFormat("#0.00");
        String formattedRating = df.format(averageRating);

        this.rating = Double.parseDouble(formattedRating);

    }
}