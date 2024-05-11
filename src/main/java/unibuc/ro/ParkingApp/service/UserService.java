package unibuc.ro.ParkingApp.service;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import unibuc.ro.ParkingApp.configuration.OIDC.Keycloak.KeycloakAdminService;
import unibuc.ro.ParkingApp.exception.OIDCUserNotFound;
import unibuc.ro.ParkingApp.exception.UserNotFound;
import unibuc.ro.ParkingApp.model.user.*;
import unibuc.ro.ParkingApp.repository.OIDCUserMappingRepository;
import unibuc.ro.ParkingApp.repository.UserRepository;
import unibuc.ro.ParkingApp.service.mapper.UserMapper;
import org.springframework.core.io.Resource;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
@Log
public class UserService {
    UserRepository repository;
    OIDCUserMappingRepository oidcUserMappingRepository;
    UserMapper userMapper;
    KeycloakAdminService keycloakAdminService;
    FileService fileService;

    public List<User> getAllUsers (){
        List<User> usersFromDB = repository.findAll();
        return usersFromDB.stream().toList();

    }

    public User updateUser(String tokenSubClaim, UpdateUserRequest updateUserRequest){
        log.info("Updating user...");
        OIDCUserMapping oidcUserMapping = tryToGetOIDCUserMapping(tokenSubClaim);
        User existingUser = oidcUserMapping.getUser();
        userMapper.fill(updateUserRequest, existingUser);
        repository.save(existingUser);
        // TODO decide whether keycloak admin should be used when updating users ( should keycloak values be different?)
//        keycloakAdminService.updateUser(tokenSubClaim, existingUser.getUsername(), existingUser.getEmail());
        log.info("User successfully updated!");
        return existingUser;
    }

    public void deleteUser(String tokenSubClaim){
        OIDCUserMapping oidcUserMapping = tryToGetOIDCUserMapping(tokenSubClaim);
        keycloakAdminService.deleteUser(tokenSubClaim);
        oidcUserMappingRepository.delete(oidcUserMapping);
        repository.delete(oidcUserMapping.getUser());

    }

    public User createUser( JwtAuthenticationToken token){
        CreateUserRequest createUserRequest = new CreateUserRequest((String) token.getTokenAttributes().get("name"), (String) token.getTokenAttributes().get("email"));
        User user = userMapper.userRequestToUser(createUserRequest);
        repository.save(user);
        oidcUserMappingRepository.save(new OIDCUserMapping((String) token.getTokenAttributes().get("sub"), user));
        return user;

    }
    public MinimalUser getProfilePicturePath(UUID id){
        User user = tryToGetUser(id);
        return new MinimalUser(user.getUsername(), null);
    }

    public User getUserById(UUID uuid){
        return tryToGetUser(uuid);
    }
    public User getUserProfile(String tokenSubClaim){

        log.info("Getting user profile ...");
        OIDCUserMapping oidcUserMapping = tryToGetOIDCUserMapping(tokenSubClaim);
        return oidcUserMapping.getUser();
    }
    public void updateUserRating(User user){
        user.computeNewRating();
        repository.save(user);
    }
    public void changeProfilePicture(String tokenSubClaim, MultipartFile profilePicture){
        log.info("Changing profile picture ...");
        OIDCUserMapping oidcUserMapping = tryToGetOIDCUserMapping(tokenSubClaim);
        User user = oidcUserMapping.getUser();
        user.setProfilePictureBytes(fileService.extractFileBytes(profilePicture));
        user.setHasProfilePicture(true);
        repository.save(user);
    }

    private User tryToGetUser(UUID uuid){
        Optional<User> userFromDB = repository.findById(uuid);
        if (userFromDB.isEmpty()){
            throw new UserNotFound(uuid.toString());
        }
        return userFromDB.get();
    }
    private OIDCUserMapping tryToGetOIDCUserMapping(String tokenSubClaim){
        Optional<OIDCUserMapping> oidcUserMapping = oidcUserMappingRepository.findById(tokenSubClaim);
        if (oidcUserMapping.isEmpty())
        {
            log.warning("Cannot map OIDC user with sub claim " + tokenSubClaim + " against any existing user. Consider creating the mapping and try again.");
            throw new OIDCUserNotFound(tokenSubClaim);
        }
        return oidcUserMapping.get();
    }


}
