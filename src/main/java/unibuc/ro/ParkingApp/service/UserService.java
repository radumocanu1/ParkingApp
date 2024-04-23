package unibuc.ro.ParkingApp.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import unibuc.ro.ParkingApp.exception.UserNotFound;
import unibuc.ro.ParkingApp.model.user.MinimalUser;
import unibuc.ro.ParkingApp.model.user.User;
import unibuc.ro.ParkingApp.model.user.UserRequest;
import unibuc.ro.ParkingApp.repository.UserRepository;
import unibuc.ro.ParkingApp.service.mapper.UserMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class UserService {

    UserRepository repository;
    UserMapper userMapper;

    public List<User> getAllUsers (){
        List<User> usersFromDB = repository.findAll();
        return usersFromDB.stream().toList();

    }

    public User updateUser(UserRequest userRequest, UUID userUUID){
        User existingUser = tryToGetUser(userUUID);
        userMapper.fill(userRequest, existingUser);
        repository.save(existingUser);
        return existingUser;
    }

    public void deleteUser(UUID userUUID){
        User user = tryToGetUser(userUUID);
        repository.delete(user);
    }

    public User createUser(UserRequest userRequest){
        User user = userMapper.userRequestToUser(userRequest);
        repository.save(user);
        return user;

    }
    public MinimalUser getProfilePicturePath(UUID id){
        User user = tryToGetUser(id);
        return new MinimalUser(user.getUsername(), user.getProfilePicturePath());
    }

    public User getUserById(UUID uuid){
        return tryToGetUser(uuid);
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

}
