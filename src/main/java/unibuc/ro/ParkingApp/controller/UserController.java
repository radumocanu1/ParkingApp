package unibuc.ro.ParkingApp.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import unibuc.ro.ParkingApp.model.User;
import unibuc.ro.ParkingApp.service.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")

public class UserController {
    @Autowired
    UserService userService;

    @PostMapping()
    public ResponseEntity<User> createUser(@Validated @RequestBody User user){
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.OK);

    }
    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers(){
        return userService.getAllUsers();
    }
    @GetMapping("/{uuid}")
    public ResponseEntity<User> getUser(@PathVariable UUID uuid){
        return new ResponseEntity<>(userService.getUserById(uuid), HttpStatus.OK);

    }
    @GetMapping("/profilePic/{uuid}")
    public ResponseEntity<String> getUserProfilePic(@PathVariable UUID uuid){
        return new ResponseEntity<>(userService.getProfilePicturePath(uuid), HttpStatus.OK);
    }

}
