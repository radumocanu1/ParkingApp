package unibuc.ro.ParkingApp.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackRequest {
    @NotNull(message = "Author feedback is required")
    private UUID feedbackAuthor;
    private String message;
    @NotNull(message = "You are required to provide a rating")
    private int ratingGiven;
    private boolean isAnonymous;

}
