package unibuc.ro.ParkingApp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    Date startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    Date endDate;
    String carNumber;

}