package unibuc.ro.ParkingApp.model.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import unibuc.ro.ParkingApp.validator.UserEmailValidator;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @NotBlank(message = "Username is required")
    String username;
    @NotBlank(message = "Email is required")
    @UserEmailValidator
    String email;
    private String profilePicturePath;
}
