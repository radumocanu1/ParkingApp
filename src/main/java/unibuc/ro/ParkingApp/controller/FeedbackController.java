package unibuc.ro.ParkingApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import unibuc.ro.ParkingApp.model.feedback.Feedback;
import unibuc.ro.ParkingApp.model.feedback.FeedbackRequest;
import unibuc.ro.ParkingApp.service.FeedbackService;

import java.util.UUID;

@Controller
@RequestMapping("feedback")
public class FeedbackController {

    @Autowired
    FeedbackService feedbackService;

    @PostMapping("/{id_of_user_to_receive_feedback}")
    public ResponseEntity<Feedback> addFeedback( @Validated @RequestBody FeedbackRequest feedbackRequest, @PathVariable UUID id_of_user_to_receive_feedback) {
        return new ResponseEntity<>(feedbackService.addFeedback(feedbackRequest, id_of_user_to_receive_feedback), HttpStatus.CREATED);
    }


}
