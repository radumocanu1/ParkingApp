package unibuc.ro.ParkingApp.service;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import unibuc.ro.ParkingApp.configuration.OIDC.Keycloak.KeycloakAdminService;
import unibuc.ro.ParkingApp.exception.UserNotFound;
import unibuc.ro.ParkingApp.model.chat.MinimalChat;
import unibuc.ro.ParkingApp.model.listing.Listing;
import unibuc.ro.ParkingApp.model.user.*;
import unibuc.ro.ParkingApp.repository.UserRepository;
import unibuc.ro.ParkingApp.service.mapper.UserMapper;


import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@Service
@Log
public class UserService {
    private final UserRepository userRepository;
    UserRepository repository;
    UserMapper userMapper;
    KeycloakAdminService keycloakAdminService;
    FileService fileService;
    OIDCUserMappingService oidcUserMappingService;

    public List<MinimalUser> getMostAppreciatedUsers() {
        List<User> usersFromDB = repository.findAll();
        return usersFromDB.stream()
                .sorted(Comparator.comparing(User::getRating).reversed())
                .limit(10)
                .map(user -> {
                    MinimalUser minimalUser = userMapper.userToMinimalUser(user);
                    if (user.isHasProfilePicture()) {
                        minimalUser.setProfilePicture(fileService.loadPicture(user.getProfilePicturePath()));
                    }
                    return minimalUser;
                })
                .collect(Collectors.toList());
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
        oidcUserMappingService.delete(oidcUserMapping);
        repository.delete(oidcUserMapping.getUser());
        keycloakAdminService.deleteUser(tokenSubClaim);
        log.info("User successfully deleted");

    }

    public List<Listing> getUserListings(String tokenSubClaim){
        log.info("Getting user listings...");
        OIDCUserMapping oidcUserMapping = oidcUserMappingService.findBySubClaim(tokenSubClaim);
        User user = oidcUserMapping.getUser();
        return user.getListings();
    }
    public UserProfilePictureResponse getUserProfilePicture(String tokenSubClaim){
        log.info("Getting user profile picture...");
        UUID userUUID = oidcUserMappingService.findBySubClaim(tokenSubClaim).getUser().getUserUUID();
        UserProfilePictureDTO userProfilePictureDTO = userRepository.findProfilePictureInfoByUserUUID(userUUID);
        if (userProfilePictureDTO.isHasProfilePicture())
            return new UserProfilePictureResponse(fileService.loadPicture(userProfilePictureDTO.getProfilePicturePath()));
        return new UserProfilePictureResponse(null);
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
    public UserResponse getUserResponseById(String tokenSubClaim,UUID uuid){
        User user = tryToGetUser(uuid);
        UserResponse userResponse = userMapper.userToUserResponse(user);
        if (oidcUserMappingService.findBySubClaim(tokenSubClaim).getUser().getUserUUID().equals(uuid))
            userResponse.setSameUser(true);
        if(user.isHasProfilePicture())
            userResponse.setProfilePictureBytes(fileService.loadPicture(user.getProfilePicturePath()));
        return userResponse;
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
    public void deleteProfilePicture(String tokenSubClaim){
        log.info("Deleting user profile picture...");
        User user = oidcUserMappingService.findBySubClaim(tokenSubClaim).getUser();
        fileService.deleteFile(user.getProfilePicturePath());
        user.setProfilePicturePath(null);
        user.setHasProfilePicture(false);
        repository.save(user);
        log.info("User profile picture successfully deleted");
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
    public void saveUser(User user){
        repository.save(user);
    }
    public MinimalUser createMinimalUser(User user){
        MinimalUser minimalUser = userMapper.userToMinimalUser(user);
        if (user.isHasProfilePicture())
            minimalUser.setProfilePicture(fileService.loadPicture(user.getProfilePicturePath()));
        return minimalUser;
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
