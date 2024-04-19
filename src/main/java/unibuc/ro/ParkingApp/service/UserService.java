package unibuc.ro.ParkingApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import unibuc.ro.ParkingApp.exception.UserNotFound;
import unibuc.ro.ParkingApp.model.Feedback;
import unibuc.ro.ParkingApp.model.User;
import unibuc.ro.ParkingApp.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    public ResponseEntity<List<User>> getAllUsers (){
        List<User> usersFromDB = repository.findAll();
        return new ResponseEntity<>(usersFromDB.stream().toList(), HttpStatus.OK);

    }

    public User createUser(User user){
        repository.save(user);
        return user;

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
