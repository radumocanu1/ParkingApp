package unibuc.ro.ParkingApp.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    UUID userUUID;
    String username;
    String email;
    private boolean isTrusted;
    private double rating;
    private boolean hasProfilePicture ;
    private byte[] profilePictureBytes;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String sex;
    private int age;
    private boolean sameUser;

}
