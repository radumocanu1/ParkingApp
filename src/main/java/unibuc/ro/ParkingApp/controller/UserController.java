package unibuc.ro.ParkingApp.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import unibuc.ro.ParkingApp.model.User;
import unibuc.ro.ParkingApp.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")

public class UserController {
    @Autowired
    UserService userService;

    @PostMapping()
    public ResponseEntity<User> createUser(@Validated @RequestBody User user){
        return userService.createUser(user);
    }
    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers(){
        return userService.getAllUsers();
    }

}
