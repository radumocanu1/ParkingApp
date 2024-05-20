package unibuc.ro.ParkingApp.model.chat;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ChatRequest {
    UUID user1UUID;
    UUID user2UUID;
}
