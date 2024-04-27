package unibuc.ro.ParkingApp.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Table(name = "oidc_user_mappping")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class OIDCUserMapping {
    @Id
    String OIDCUserUUID;
    @OneToOne
    User user;

}
