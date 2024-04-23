package unibuc.ro.ParkingApp.service.mapper;

import org.mapstruct.Mapper;
import unibuc.ro.ParkingApp.model.feedback.Feedback;
import unibuc.ro.ParkingApp.model.feedback.FeedbackRequest;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {
    Feedback feedbackRequestToFeedback(FeedbackRequest feedbackRequest);
}
