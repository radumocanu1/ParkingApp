package unibuc.ro.ParkingApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import unibuc.ro.ParkingApp.model.feedback.Feedback;
import unibuc.ro.ParkingApp.model.feedback.FeedbackRequest;
import unibuc.ro.ParkingApp.model.feedback.FeedbackResponse;
import unibuc.ro.ParkingApp.service.FeedbackService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("feedback")
public class FeedbackController {

    @Autowired
    FeedbackService feedbackService;

    @PostMapping("/{listingUUID}")
    public ResponseEntity<FeedbackResponse> addFeedback(@Validated @RequestBody FeedbackRequest feedbackRequest, @PathVariable String listingUUID, Principal principal) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        return new ResponseEntity<>(feedbackService.addFeedback((String) token.getTokenAttributes().get("sub"),feedbackRequest, UUID.fromString(listingUUID)), HttpStatus.CREATED);
    }
    @GetMapping("/{listingUUID}")
    public ResponseEntity<List<FeedbackResponse>> getFeedback(@PathVariable String listingUUID) {
        return new ResponseEntity<>(feedbackService.getAllListingFeedbacks(UUID.fromString(listingUUID)),HttpStatus.OK);
    }
    @GetMapping()
    public ResponseEntity<List<FeedbackResponse>> getAllUserFeedbacks(Principal principal) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        return new ResponseEntity<>(feedbackService.getAllUserFeedbacks((String) token.getTokenAttributes().get("sub")),HttpStatus.OK);
    }

}
