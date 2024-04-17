package unibuc.ro.ParkingApp.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackRequest {
    private UUID userToReceiveFeedback;
    private UUID feedbackAuthor ;
    private String message;
    private int ratingGiven;

}
