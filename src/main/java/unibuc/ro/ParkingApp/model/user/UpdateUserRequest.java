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
    private String phoneNumber;
    private String userName;
    private int age;
    private String sex;
}
