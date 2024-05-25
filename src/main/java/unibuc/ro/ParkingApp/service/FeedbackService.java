package unibuc.ro.ParkingApp.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unibuc.ro.ParkingApp.model.feedback.Feedback;
import unibuc.ro.ParkingApp.model.feedback.FeedbackRequest;
import unibuc.ro.ParkingApp.model.feedback.FeedbackResponse;
import unibuc.ro.ParkingApp.model.listing.Listing;
import unibuc.ro.ParkingApp.model.user.User;
import unibuc.ro.ParkingApp.repository.FeedbackRepository;
import unibuc.ro.ParkingApp.service.mapper.FeedbackMapper;

import java.util.UUID;

@Service
@AllArgsConstructor
public class FeedbackService {

    FeedbackRepository feedbackRepository;
    ListingService listingService;
    OIDCUserMappingService oidcUserMappingService;
    FeedbackMapper feedbackMapper;
    FileService fileService;
    @Transactional
    public FeedbackResponse addFeedback(String tokenSubClaim, FeedbackRequest feedbackRequest, UUID listingUUID) {
        Listing listing = listingService.getListing(listingUUID);
        Feedback feedback = feedbackMapper.feedbackRequestToFeedback(feedbackRequest);

        // Salvează feedback-ul înainte de a-l adăuga la listing
        feedback.setListing(listing); // Asociază feedback-ul cu listing-ul
        feedback = feedbackRepository.save(feedback); // Salvează feedback-ul în baza de date

        listing.addFeedback(feedback);
        listingService.saveListing(listing);

        return createFeedbackResponse(feedback, oidcUserMappingService.findBySubClaim(tokenSubClaim).getUser());
    }
    private FeedbackResponse createFeedbackResponse(Feedback feedback, User user) {
        FeedbackResponse feedbackResponse = feedbackMapper.feedbackToFeedbackResponse(feedback);
        if (user.isHasProfilePicture())
            feedbackResponse.setUserProfilePicture(fileService.loadPicture(user.getProfilePicturePath()));
        return feedbackResponse;
    }

}
