package unibuc.ro.ParkingApp.service;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import unibuc.ro.ParkingApp.configuration.OIDC.Keycloak.KeycloakAdminService;
import unibuc.ro.ParkingApp.exception.OIDCUserNotFound;
import unibuc.ro.ParkingApp.exception.UserNotFound;
import unibuc.ro.ParkingApp.model.user.*;
import unibuc.ro.ParkingApp.repository.OIDCUserMappingRepository;
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
    OIDCUserMappingRepository oidcUserMappingRepository;
    UserMapper userMapper;
    KeycloakAdminService keycloakAdminService;

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
        keycloakAdminService.updateUser(tokenSubClaim, existingUser.getUsername(), existingUser.getEmail());
        log.info("User successfully updated!");
        return existingUser;
    }

    public void deleteUser(String tokenSubClaim){
        OIDCUserMapping oidcUserMapping = tryToGetOIDCUserMapping(tokenSubClaim);
        keycloakAdminService.deleteUser(tokenSubClaim);
        oidcUserMappingRepository.delete(oidcUserMapping);
        repository.delete(oidcUserMapping.getUser());

    }

    public User createUser(String tokenSubClaim, CreateUserRequest createUserRequest){
        User user = userMapper.userRequestToUser(createUserRequest);
        repository.save(user);
        oidcUserMappingRepository.save(new OIDCUserMapping(tokenSubClaim, user));
        return user;

    }
    public MinimalUser getProfilePicturePath(UUID id){
        User user = tryToGetUser(id);
        return new MinimalUser(user.getUsername(), user.getProfilePicturePath());
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
