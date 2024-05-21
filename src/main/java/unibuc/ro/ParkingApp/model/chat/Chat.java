package unibuc.ro.ParkingApp.model.chat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "chat")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID chatUUID;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();

    UUID user1UUID;
    UUID user2UUID;

    public List<Message> getMessages() {
        messages.sort(Comparator.comparing(Message::getTimestamp));
        return messages;
    }
}