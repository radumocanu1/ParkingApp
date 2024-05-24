package unibuc.ro.ParkingApp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "pending_payment", indexes = {
        @Index(columnList = "user_id", name = "index_user")
})
public class PendingPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID paymentUUID;
    @Column(name = "user_id")
    UUID userUUID;
    UUID listingUUID;

}
