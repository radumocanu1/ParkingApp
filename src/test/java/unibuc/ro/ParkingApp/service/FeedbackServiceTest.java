package unibuc.ro.ParkingApp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import unibuc.ro.ParkingApp.model.feedback.Feedback;
import unibuc.ro.ParkingApp.model.feedback.FeedbackRequest;
import unibuc.ro.ParkingApp.model.feedback.FeedbackResponse;
import unibuc.ro.ParkingApp.model.listing.Listing;
import unibuc.ro.ParkingApp.model.user.OIDCUserMapping;
import unibuc.ro.ParkingApp.model.user.User;
import unibuc.ro.ParkingApp.repository.FeedbackRepository;
import unibuc.ro.ParkingApp.service.mapper.FeedbackMapper;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FeedbackServiceTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private ListingService listingService;

    @Mock
    private OIDCUserMappingService oidcUserMappingService;

    @Mock
    private FeedbackMapper feedbackMapper;

    @Mock
    private FileService fileService;

    @Mock
    private UserService userService;

    @InjectMocks
    private FeedbackService feedbackService;

    private Feedback feedback;
    private FeedbackRequest feedbackRequest;
    private Listing listing;
    private User user;
    private UUID listingUUID;
    private UUID userUUID;
    private OIDCUserMapping oidcUserMapping;

    @BeforeEach
    void setUp() {
        listingUUID = UUID.randomUUID();
        userUUID = UUID.randomUUID();

        user = new User();
        user.setUserUUID(userUUID);
        user.setHasProfilePicture(true);
        user.setProfilePicturePath("path/to/profile/picture");

        oidcUserMapping = new OIDCUserMapping();
        oidcUserMapping.setOIDCUserUUID(UUID.randomUUID().toString());
        oidcUserMapping.setUser(user);

        listing = new Listing();
        listing.setListingUUID(listingUUID);
        listing.setUser(user);

        feedback = new Feedback();
        feedback.setFeedbackUUID(UUID.randomUUID());
        feedback.setListing(listing);
        feedback.setAuthorUUID(userUUID);
        feedback.setPublishingDate(LocalDateTime.now());

        feedbackRequest = new FeedbackRequest();
    }

    @Test
    void addFeedback_ShouldAddFeedbackAndReturnResponse() {
        when(listingService.getListing(listingUUID)).thenReturn(listing);
        when(feedbackMapper.feedbackRequestToFeedback(any(FeedbackRequest.class))).thenReturn(feedback);
        when(oidcUserMappingService.findBySubClaim(anyString())).thenReturn(oidcUserMapping);
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);
        when(feedbackMapper.feedbackToFeedbackResponse(any(Feedback.class))).thenReturn(new FeedbackResponse());
        when(fileService.loadPicture(anyString())).thenReturn(new byte[0]);

        FeedbackResponse response = feedbackService.addFeedback("sub-claim", feedbackRequest, listingUUID);

        assertNotNull(response);
        verify(feedbackRepository, times(1)).save(any(Feedback.class));
        verify(listingService, times(1)).saveListing(any(Listing.class));
    }

    @Test
    void getAllListingFeedbacks_ShouldReturnFeedbackResponses() {
        when(listingService.getListing(listingUUID)).thenReturn(listing);
        listing.addFeedback(feedback);
        when(userService.getUserById(any(UUID.class))).thenReturn(user);
        when(feedbackMapper.feedbackToFeedbackResponse(any(Feedback.class))).thenReturn(new FeedbackResponse());
        when(fileService.loadPicture(anyString())).thenReturn(new byte[0]);

        List<FeedbackResponse> responses = feedbackService.getAllListingFeedbacks(listingUUID);

        assertFalse(responses.isEmpty());
        verify(listingService, times(1)).getListing(listingUUID);
        verify(feedbackMapper, times(1)).feedbackToFeedbackResponse(any(Feedback.class));
    }

    @Test
    void getAllUserFeedbacks_ShouldReturnFeedbackResponses() {
        when(oidcUserMappingService.findBySubClaim(anyString())).thenReturn(oidcUserMapping);
        when(userService.getUserById(any(UUID.class))).thenReturn(user);
        listing.addFeedback(feedback);
        user.setListings(List.of(listing));
        when(feedbackMapper.feedbackToFeedbackResponse(any(Feedback.class))).thenReturn(new FeedbackResponse());
        when(fileService.loadPicture(anyString())).thenReturn(new byte[0]);

        List<FeedbackResponse> responses = feedbackService.getAllUserFeedbacks("sub-claim");

        assertFalse(responses.isEmpty());
        verify(oidcUserMappingService, times(1)).findBySubClaim(anyString());
        verify(feedbackMapper, times(1)).feedbackToFeedbackResponse(any(Feedback.class));
    }

    // Adaugă alte teste pentru celelalte metode ale FeedbackService

}
