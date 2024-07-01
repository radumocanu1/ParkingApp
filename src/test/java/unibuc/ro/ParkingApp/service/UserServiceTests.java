package unibuc.ro.ParkingApp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import unibuc.ro.ParkingApp.configuration.OIDC.Keycloak.KeycloakAdminService;
import unibuc.ro.ParkingApp.exception.UserNotFound;
import unibuc.ro.ParkingApp.model.user.*;
import unibuc.ro.ParkingApp.repository.UserRepository;
import unibuc.ro.ParkingApp.service.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private KeycloakAdminService keycloakAdminService;

    @Mock
    private FileService fileService;

    @Mock
    private OIDCUserMappingService oidcUserMappingService;

    @InjectMocks
    private UserService userService;

    private User user;
    private OIDCUserMapping oidcUserMapping;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserUUID(UUID.randomUUID());
        user.setRating(4.5);
        user.setHasProfilePicture(true);
        user.setProfilePicturePath("path/to/profile/picture");

        oidcUserMapping = new OIDCUserMapping();
        oidcUserMapping.setUser(user);
    }

    @Test
    void getMostAppreciatedUsers_ShouldReturnSortedMinimalUsers() {
        List<User> usersFromDB = new ArrayList<>();
        usersFromDB.add(user);

        when(userRepository.findAll()).thenReturn(usersFromDB);
        when(userMapper.userToMinimalUser(any(User.class))).thenReturn(new MinimalUser());

        List<MinimalUser> result = userService.getMostAppreciatedUsers();

        assertFalse(result.isEmpty());
        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).userToMinimalUser(any(User.class));
    }


    @Test
    void deleteUser_ShouldDeleteUser() {
        String tokenSubClaim = "sub-claim";

        when(oidcUserMappingService.findBySubClaim(tokenSubClaim)).thenReturn(oidcUserMapping);

        userService.deleteUser(tokenSubClaim);

        verify(oidcUserMappingService, times(1)).delete(any(OIDCUserMapping.class));
        verify(userRepository, times(1)).delete(any(User.class));
        verify(keycloakAdminService, times(1)).deleteUser(tokenSubClaim);
    }

    @Test
    void getUserById_ShouldReturnUser() {
        UUID uuid = UUID.randomUUID();

        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));

        User result = userService.getUserById(uuid);

        assertEquals(user, result);
        verify(userRepository, times(1)).findById(uuid);
    }

    @Test
    void getUserById_ShouldThrowUserNotFound() {
        UUID uuid = UUID.randomUUID();

        when(userRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(UserNotFound.class, () -> userService.getUserById(uuid));
        verify(userRepository, times(1)).findById(uuid);
    }

    // Adaugă alte teste pentru celelalte metode ale UserService
}
