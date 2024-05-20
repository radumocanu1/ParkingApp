package unibuc.ro.ParkingApp.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import unibuc.ro.ParkingApp.configuration.ApplicationConstants;
import unibuc.ro.ParkingApp.model.chat.Chat;
import unibuc.ro.ParkingApp.model.user.UpdateUserRequest;
import unibuc.ro.ParkingApp.model.user.User;
import unibuc.ro.ParkingApp.model.user.UserResponse;
import unibuc.ro.ParkingApp.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")

public class UserController {
    @Autowired
    UserService userService;

    @PostMapping()
    public ResponseEntity<User> createUser(Principal principal){
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        return new ResponseEntity<>(userService.createUser((String) token.getTokenAttributes().get("sub"), (String) token.getTokenAttributes().get("name"), (String) token.getTokenAttributes().get("email")), HttpStatus.OK);

    }
    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }
    @GetMapping("/{uuid}")
    public ResponseEntity<User> getUser(@PathVariable UUID uuid){
        return new ResponseEntity<>(userService.getUserById(uuid), HttpStatus.OK);

    }
    @PutMapping()
    public ResponseEntity<User> updateUser(@Validated @RequestBody UpdateUserRequest updateUserRequest, Principal principal){
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        return new ResponseEntity<>(userService.updateUser((String) token.getTokenAttributes().get("sub"), updateUserRequest), HttpStatus.OK);

    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteUser(Principal principal){
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        userService.deleteUser((String) token.getTokenAttributes().get("sub"));
        return new ResponseEntity<>( HttpStatus.NO_CONTENT);

    }
    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getUserProfile(Principal principal){
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        return new ResponseEntity<>(userService.getUserProfile((String) token.getTokenAttributes().get("sub")), HttpStatus.OK);
    }
    @PostMapping("/profilePic")
    public ResponseEntity<String> updateUserProfilePic(Principal principal, @RequestParam("file")  MultipartFile multipartFile){
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        String subClaim = (String) token.getTokenAttributes().get("sub");
        userService.changeProfilePicture(subClaim, multipartFile);
        return new ResponseEntity<>(String.format(ApplicationConstants.PROFILE_PICTURE_UPDATED, subClaim), HttpStatus.OK);
    }


}
