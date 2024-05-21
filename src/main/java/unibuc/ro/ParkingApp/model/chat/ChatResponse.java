package unibuc.ro.ParkingApp.model.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatResponse {
    UUID chatUUID;
    UUID otherUserUUID;
    byte[] otherUserProfileImage;
    String otherUserName;
    private List<Message> messages = new ArrayList<>();
}
