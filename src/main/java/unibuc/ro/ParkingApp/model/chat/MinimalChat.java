package unibuc.ro.ParkingApp.model.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MinimalChat {
    UUID chatUUID;
    String otherUsername;
    byte[] otherUserProfilePicture;
    boolean hasUnreadMessages;
    String lastMessage;
}
