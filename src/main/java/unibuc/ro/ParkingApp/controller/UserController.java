package unibuc.ro.ParkingApp.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import unibuc.ro.ParkingApp.model.user.MinimalUser;
import unibuc.ro.ParkingApp.model.user.User;
import unibuc.ro.ParkingApp.model.user.UserRequest;
import unibuc.ro.ParkingApp.service.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")

public class UserController {
    @Autowired
    UserService userService;

    @PostMapping()
    public ResponseEntity<User> createUser(@Validated @RequestBody UserRequest userRequest){
        return new ResponseEntity<>(userService.createUser(userRequest), HttpStatus.OK);

    }
    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }
    @GetMapping("/{uuid}")
    public ResponseEntity<User> getUser(@PathVariable UUID uuid){
        return new ResponseEntity<>(userService.getUserById(uuid), HttpStatus.OK);

    }
    @GetMapping("/profilePic/{uuid}")
    public ResponseEntity<MinimalUser> getUserProfilePic(@PathVariable UUID uuid){
        return new ResponseEntity<>(userService.getProfilePicturePath(uuid), HttpStatus.OK);
    }
    @PutMapping("/{uuid}")
    public ResponseEntity<User> updateUser(@PathVariable UUID uuid, @Validated @RequestBody UserRequest userRequest){
        return new ResponseEntity<>(userService.updateUser(userRequest, uuid), HttpStatus.OK);

    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> updateUser(@PathVariable UUID uuid){
        userService.deleteUser(uuid);
        return new ResponseEntity<>( HttpStatus.OK);

    }

}
