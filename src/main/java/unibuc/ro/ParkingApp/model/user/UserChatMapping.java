package unibuc.ro.ParkingApp.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.AnyDiscriminatorValue;

import java.util.UUID;

// sql correct table definition

/* CREATE TABLE user_chats_mapping (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        useruuid BINARY(16) NOT NULL,
chat_key BINARY(16) NOT NULL,
chat_value BINARY(16)
) ENGINE=InnoDB;
*/


@Entity
@Table(name = "user_chats_mapping")
@AllArgsConstructor
@NoArgsConstructor
public class UserChatMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "userUUID", nullable = false)
    private UUID userUUID;

    @Column(name = "chatUUID", nullable = false)
    private UUID chatKey;

}