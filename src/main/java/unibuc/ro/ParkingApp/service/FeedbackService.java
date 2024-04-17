package unibuc.ro.ParkingApp.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import unibuc.ro.ParkingApp.model.Feedback;
import unibuc.ro.ParkingApp.model.FeedbackRequest;
import unibuc.ro.ParkingApp.repository.FeedbackRepository;

@Service
@AllArgsConstructor
public class FeedbackService {

    FeedbackRepository feedbackRepository;
    UserService userService;

}
