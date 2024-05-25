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

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class FeedbackService {

    FeedbackRepository feedbackRepository;
    ListingService listingService;
    OIDCUserMappingService oidcUserMappingService;
    FeedbackMapper feedbackMapper;
    FileService fileService;
    UserService userService;
    @Transactional
    public FeedbackResponse addFeedback(String tokenSubClaim, FeedbackRequest feedbackRequest, UUID listingUUID) {
        Listing listing = listingService.getListing(listingUUID);
        Feedback feedback = feedbackMapper.feedbackRequestToFeedback(feedbackRequest);
        feedback.setPublishingDate(LocalDateTime.now());
        User user = oidcUserMappingService.findBySubClaim(tokenSubClaim).getUser();
        feedback.setAuthorUUID(user.getUserUUID());

        feedback.setListing(listing);
        feedback = feedbackRepository.save(feedback);

        listing.addFeedback(feedback);
        listingService.saveListing(listing);

        return createFeedbackResponse(feedback, user);
    }
    public List<FeedbackResponse> getAllListingFeedbacks(UUID listingUUID) {
        Listing listing = listingService.getListing(listingUUID);
        return listing.getFeedbackList().stream()
                .map((feedback) -> createFeedbackResponse(feedback,userService.getUserById(feedback.getAuthorUUID())))
                .toList();
    }
    public List<FeedbackResponse> getAllUserFeedbacks(String tokenSubClaim) {
        User user = oidcUserMappingService.findBySubClaim(tokenSubClaim).getUser();
        List<Listing> userListings = user.getListings();
        List<Feedback> feedbacks = new ArrayList<>();
        for (Listing listing : userListings) {
            feedbacks.addAll(listing.getFeedbackList());
        }
        return feedbacks.stream()
                .sorted(Comparator.comparing(Feedback::getPublishingDate).reversed())
                .map((feedback -> createFeedbackResponse(feedback,user))).toList();

    }

    private FeedbackResponse createFeedbackResponse(Feedback feedback, User user) {
        FeedbackResponse feedbackResponse = feedbackMapper.feedbackToFeedbackResponse(feedback);
        if (user.isHasProfilePicture())
            feedbackResponse.setUserProfilePicture(fileService.loadPicture(user.getProfilePicturePath()));
        return feedbackResponse;
    }

}
