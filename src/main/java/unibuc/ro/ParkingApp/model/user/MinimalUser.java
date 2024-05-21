package unibuc.ro.ParkingApp.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MinimalUser {
    UUID userUUID;
    String username;
    private byte[] profilePicture;

    private double rating;
}
