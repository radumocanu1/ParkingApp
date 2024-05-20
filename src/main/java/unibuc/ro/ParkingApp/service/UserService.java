package unibuc.ro.ParkingApp.service;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import unibuc.ro.ParkingApp.configuration.OIDC.Keycloak.KeycloakAdminService;
import unibuc.ro.ParkingApp.exception.OIDCUserNotFound;
import unibuc.ro.ParkingApp.exception.UserNotFound;
import unibuc.ro.ParkingApp.model.chat.Chat;
import unibuc.ro.ParkingApp.model.listing.Listing;
import unibuc.ro.ParkingApp.model.user.*;
import unibuc.ro.ParkingApp.repository.ChatRepository;
import unibuc.ro.ParkingApp.repository.UserRepository;
import unibuc.ro.ParkingApp.service.mapper.UserMapper;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
@Log
public class UserService {
    UserRepository repository;
    UserMapper userMapper;
    KeycloakAdminService keycloakAdminService;
    FileService fileService;
    OIDCUserMappingService oidcUserMappingService;

    public List<User> getAllUsers (){
        List<User> usersFromDB = repository.findAll();
        return usersFromDB.stream().toList();

    }

    public User updateUser(String tokenSubClaim, UpdateUserRequest updateUserRequest){
        log.info("Updating user... " + tokenSubClaim);
        OIDCUserMapping oidcUserMapping = oidcUserMappingService.findBySubClaim(tokenSubClaim);
        User existingUser = oidcUserMapping.getUser();
        userMapper.fill(updateUserRequest, existingUser);
        repository.save(existingUser);
        // TODO decide whether keycloak admin should be used when updating users ( should keycloak values be different?)
//        keycloakAdminService.updateUser(tokenSubClaim, existingUser.getUsername(), existingUser.getEmail());
        log.info("User successfully updated!");
        return existingUser;
    }

    public void deleteUser(String tokenSubClaim){
        log.info("Deleting user... " + tokenSubClaim);
        OIDCUserMapping oidcUserMapping = oidcUserMappingService.findBySubClaim(tokenSubClaim);
        keycloakAdminService.deleteUser(tokenSubClaim);
        oidcUserMappingService.delete(oidcUserMapping);
        repository.delete(oidcUserMapping.getUser());
        log.info("User successfully deleted");

    }
    public List<Listing> getUserListings(String tokenSubClaim){
        log.info("Getting user listings...");
        OIDCUserMapping oidcUserMapping = oidcUserMappingService.findBySubClaim(tokenSubClaim);
        User user = oidcUserMapping.getUser();
        return user.getListings();
    }
    public User createUser(String tokenSubClaim, String tokenNameClaim, String tokenEmail){
        log.info("Creating user...");
        CreateUserRequest createUserRequest = new CreateUserRequest(tokenNameClaim, tokenEmail);
        User user = userMapper.userRequestToUser(createUserRequest);
        repository.save(user);
        OIDCUserMapping oidcUserMapping = oidcUserMappingService.create(tokenSubClaim, user);
        log.info("User successfully created!");
        log.info("Created mapping from OIDC sub " + oidcUserMapping.getOIDCUserUUID() + "to user id "+ oidcUserMapping.getUser().getUserUUID());

        return user;

    }

    public User getUserById(UUID uuid){
        return tryToGetUser(uuid);
    }
    public UserResponse getUserProfile(String tokenSubClaim){

        log.info("Getting user profile ... sub claim -> " + tokenSubClaim);
        User user = oidcUserMappingService.findBySubClaim(tokenSubClaim).getUser();
        log.info("User profile was successfully fetched!");
        UserResponse userResponse = userMapper.userToUserResponse(user);
        if (user.isHasProfilePicture()){
            userResponse.setProfilePictureBytes(fileService.loadPicture(user.getProfilePicturePath()));
        }
        return userResponse;
    }
    public void updateUserRating(User user){
        user.computeNewRating();
        repository.save(user);
    }

    public void changeProfilePicture(String tokenSubClaim, MultipartFile profilePicture){
        log.info("Changing profile picture... sub claim -> " + tokenSubClaim);
        OIDCUserMapping oidcUserMapping = oidcUserMappingService.findBySubClaim(tokenSubClaim);
        User user = oidcUserMapping.getUser();
        user.setProfilePicturePath(fileService.saveProfilePicture(user.getUserUUID(), profilePicture));
        user.setHasProfilePicture(true);
        repository.save(user);

        log.info("Profile picture was successfully set!");

    }
    public User saveUser(User user){
        return repository.save(user);
    }
    private User tryToGetUser(UUID uuid){
        Optional<User> userFromDB = repository.findById(uuid);
        if (userFromDB.isEmpty()){
            log.warning("User with internal id " + uuid + " was not found in DB");
            throw new UserNotFound(uuid.toString());
        }
        return userFromDB.get();
    }


}
