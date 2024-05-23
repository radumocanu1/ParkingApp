package unibuc.ro.ParkingApp.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfilePictureDTO {
    private String profilePicturePath;
    private boolean hasProfilePicture;
}
