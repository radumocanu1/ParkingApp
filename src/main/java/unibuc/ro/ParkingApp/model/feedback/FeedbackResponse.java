package unibuc.ro.ParkingApp.model.feedback;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FeedbackResponse {
    byte[] userProfilePicture;
    private UUID authorUUID;
    private String message;
    private int ratingGiven;
}
