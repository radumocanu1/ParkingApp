package unibuc.ro.ParkingApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import unibuc.ro.ParkingApp.model.Feedback;
import unibuc.ro.ParkingApp.model.FeedbackRequest;
import unibuc.ro.ParkingApp.service.FeedbackService;

@Controller
@RequestMapping("feedback")
public class FeedbackController {
    @Autowired
    FeedbackService feedbackService;


}
