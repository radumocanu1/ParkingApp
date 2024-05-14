package unibuc.ro.ParkingApp.service;



import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import unibuc.ro.ParkingApp.configuration.OIDC.Keycloak.KeycloakAdminService;
import unibuc.ro.ParkingApp.model.user.OIDCUserMapping;
import unibuc.ro.ParkingApp.model.user.UpdateUserRequest;
import unibuc.ro.ParkingApp.model.user.User;
import unibuc.ro.ParkingApp.repository.OIDCUserMappingRepository;
import unibuc.ro.ParkingApp.repository.UserRepository;
import unibuc.ro.ParkingApp.service.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Disabled
@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @InjectMocks
    UserService userService;
    @Mock
    UserRepository repository;
    @Mock
    OIDCUserMappingRepository oidcUserMappingRepository;
    @Mock
    UserMapper userMapper;
    @Mock
    KeycloakAdminService keycloakAdminService;
    @Mock
    FileService fileService;
    List<User> users;


    @BeforeEach
    public void setUserMocks(){
        users = createMockUserList();

    }

    @Test
    void shouldReturnAllUsersFromRepository() {
        when(repository.findAll()).thenReturn(createMockUserList());
        Assertions.assertEquals(userService.getAllUsers().size(), 3);
    }
    @Test
    void shouldUpdateExistingUser() {
        // given
        UpdateUserRequest updateUserRequest = new UpdateUserRequest(
                "new_username",
                "new_email",
                null,
                null,
                27,
                null
        );
        User mockUser = users.get(0);
        OIDCUserMapping oidcUserMapping = new OIDCUserMapping(UUID.randomUUID().toString(),mockUser);
        when(oidcUserMappingRepository.findById(anyString())).thenReturn(Optional.of(oidcUserMapping));
        when(repository.save(any(User.class))).thenReturn(mockUser);


        Assertions.assertEquals(userService.updateUser(UUID.randomUUID().toString(), updateUserRequest), mockUser);
    }
    @Test
    void shouldDeleteUser(){
        User mockUser = users.get(0);
        OIDCUserMapping oidcUserMapping = new OIDCUserMapping(UUID.randomUUID().toString(),mockUser);
        String subClaim = UUID.randomUUID().toString();
//        when(oidcUserMappingRepository.findById(subClaim)).thenReturn(Optional.of(oidcUserMapping));

    }

    private List<User> createMockUserList(){
        User mockUser1 = new User(
                UUID.fromString("a4db877f-5f0a-477b-856d-20e051e9fe6a"),
                "mockUser1",
                "mock1@example.com",
                new ArrayList<>(),
                new ArrayList<>(),
                true,
                4.5,
                false,
                null,
                "+1234567890",
                "John",
                "Doe",
                "Male",
                30
        );
        User mockUser2 = new User(
                UUID.randomUUID(),
                "mockUser2",
                "mock2@example.com",
                new ArrayList<>(),
                new ArrayList<>(),
                false,
                3.2,
                true,
                new byte[0],
                "+9876543210",
                "Jane",
                "Smith",
                "Female",
                25
        );
        User mockUser3 = new User(
                UUID.randomUUID(),
                "mockUser3",
                "mock3@example.com",
                new ArrayList<>(),
                new ArrayList<>(),
                true,
                2.8,
                false,
                null,
                "+5551234567",
                "Alex",
                "Brown",
                "Non-binary",
                22
        );



        return new ArrayList<>(List.of(mockUser1,mockUser2,mockUser3));

    }



}
