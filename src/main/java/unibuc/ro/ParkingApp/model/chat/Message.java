package unibuc.ro.ParkingApp.model.chat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import unibuc.ro.ParkingApp.model.user.User;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "message")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID messageUUID;

    @ManyToOne
    @JoinColumn(name = "chatUUID", nullable = false)
    @JsonIgnore
    Chat chat;

    UUID senderUUID;
    String messageContent;

    @Column(name = "timestamp", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date timestamp;

    @PrePersist
    protected void onCreate() {
        timestamp = new Date();
    }
}
