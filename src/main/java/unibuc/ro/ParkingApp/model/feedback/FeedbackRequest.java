package unibuc.ro.ParkingApp.model.feedback;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackRequest {
    private String message;
    private int ratingGiven;
}