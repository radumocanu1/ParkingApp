package unibuc.ro.ParkingApp.model.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
    String username;
    String email;
    private String profilePicturePath;
    private String phoneNumber;
    private String firstName;
    private String LastName;
    private int age;
}
