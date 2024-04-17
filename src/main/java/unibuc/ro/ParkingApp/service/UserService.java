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

    public ResponseEntity<User> createUser(User user){
        repository.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);

    }
    public ResponseEntity<User> getUserById(UUID uuid){

        return new ResponseEntity<>(tryToGetUser(uuid), HttpStatus.OK);
    }

    private User tryToGetUser(UUID uuid){
        Optional<User> userFromDB = repository.findById(uuid);
        if (userFromDB.isEmpty()){
            throw new UserNotFound(uuid.toString());
        }
        return userFromDB.get();
    }

}
