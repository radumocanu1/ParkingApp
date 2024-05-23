package unibuc.ro.ParkingApp.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserProfilePictureResponse {
    byte[] profilePictureBytes;
}
