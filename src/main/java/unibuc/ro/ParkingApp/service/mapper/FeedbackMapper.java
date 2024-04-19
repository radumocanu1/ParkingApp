package unibuc.ro.ParkingApp.service.mapper;

import org.mapstruct.Mapper;
import unibuc.ro.ParkingApp.model.Feedback;
import unibuc.ro.ParkingApp.model.FeedbackRequest;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {
    Feedback feedbackRequestToFeedback(FeedbackRequest feedbackRequest);
}
