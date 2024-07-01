package unibuc.ro.ParkingApp.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;
import unibuc.ro.ParkingApp.model.PictureType;
import unibuc.ro.ParkingApp.model.listing.*;
import unibuc.ro.ParkingApp.model.user.MinimalUser;
import unibuc.ro.ParkingApp.model.user.OIDCUserMapping;
import unibuc.ro.ParkingApp.model.user.User;
import unibuc.ro.ParkingApp.repository.ListingRepository;
import unibuc.ro.ParkingApp.service.mapper.ListingMapper;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ListingServiceTest {

    @Mock
    private ListingRepository listingRepository;

    @Mock
    private ListingMapper listingMapper;

    @Mock
    private UserService userService;

    @Mock
    private OIDCUserMappingService oidcUserMappingService;

    @Mock
    private FileService fileService;

    @Mock
    private ChatService chatService;

    @InjectMocks
    private ListingService listingService;

    @Mock
    private MultipartFile multipartFile;

    private final UUID listingUUID = UUID.randomUUID();
    private final Listing listing = new Listing();

    ListingServiceTest() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void createListing_ShouldCreateAndReturnListing() {
        ListingRequest listingRequest = new ListingRequest();
        User publishingUser = new User();
        OIDCUserMapping oidcUserMapping = new OIDCUserMapping();
        oidcUserMapping.setUser(publishingUser);

        when(listingMapper.listingRequestToListing(any(ListingRequest.class))).thenReturn(listing);
        when(oidcUserMappingService.findBySubClaim(anyString())).thenReturn(oidcUserMapping);
        when(listingRepository.save(any(Listing.class))).thenReturn(listing);

        Listing result = listingService.createListing(listingRequest, "tokenSubClaim");

        assertNotNull(result);
        assertEquals(publishingUser, result.getUser());
        assertEquals(Status.PENDING, result.getStatus());
        verify(listingRepository, times(1)).save(listing);
    }

    @Test
    void deleteListing_ShouldDeleteListing() {
        when(listingRepository.findById(listingUUID)).thenReturn(Optional.of(listing));

        listingService.deleteListing(listingUUID);

        verify(fileService, times(1)).deleteDirectory(listingUUID);
        verify(listingRepository, times(1)).delete(listing);
    }

    @Test
    void addPhotoToListing_ShouldAddPhoto() {
        when(listingRepository.findById(listingUUID)).thenReturn(Optional.of(listing));
        when(fileService.saveFile(anyString(), any(MultipartFile.class))).thenReturn("saved-file-path");

        listingService.addPhotoToListing(listingUUID, multipartFile, PictureType.MAIN_PICTURE);

        verify(fileService, times(1)).saveFile(anyString(), any(MultipartFile.class));
        verify(listingRepository, times(1)).save(listing);
    }

    @Test
    void getAllListings_ShouldReturnListOfMinimalListings() {
        List<Listing> listingsFromDB = new ArrayList<>();
        listingsFromDB.add(listing);

        when(listingRepository.findAll()).thenReturn(listingsFromDB);
        when(listingMapper.listingToMinimalListing(any(Listing.class))).thenReturn(new MinimalListing());
        when(fileService.loadPicture(anyString())).thenReturn(new byte[0]);

        List<MinimalListing> result = listingService.getAllListings();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(listingRepository, times(1)).findAll();
    }
}
