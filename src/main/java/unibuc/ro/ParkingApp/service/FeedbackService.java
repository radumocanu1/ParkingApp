package unibuc.ro.ParkingApp.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import unibuc.ro.ParkingApp.model.feedback.Feedback;
import unibuc.ro.ParkingApp.model.feedback.FeedbackRequest;
import unibuc.ro.ParkingApp.model.user.User;
import unibuc.ro.ParkingApp.repository.FeedbackRepository;
import unibuc.ro.ParkingApp.service.mapper.FeedbackMapper;

import java.util.UUID;

@Service
@AllArgsConstructor
public class FeedbackService {

    FeedbackRepository feedbackRepository;
    UserService userService;
    FeedbackMapper feedbackMapper;

    public Feedback addFeedback(FeedbackRequest feedbackRequest, UUID id_of_user_to_receive_feedback) {
        User user = userService.getUserById(id_of_user_to_receive_feedback);
        Feedback feedback = feedbackMapper.feedbackRequestToFeedback(feedbackRequest);
        feedback.setUser(user);
        feedbackRepository.save(feedback);
        userService.updateUserRating(user);
        return feedback;
    }

}
