package unibuc.ro.ParkingApp.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import unibuc.ro.ParkingApp.model.feedback.Feedback;
import unibuc.ro.ParkingApp.model.feedback.FeedbackRequest;
import unibuc.ro.ParkingApp.model.feedback.FeedbackResponse;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {
    Feedback feedbackRequestToFeedback(FeedbackRequest feedbackRequest);
    @Mapping(target = "userProfilePicture", ignore = true)
    FeedbackResponse feedbackToFeedbackResponse(Feedback feedback);
}
