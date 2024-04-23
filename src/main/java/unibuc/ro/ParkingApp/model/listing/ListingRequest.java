package unibuc.ro.ParkingApp.model.listing;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListingRequest {
    @NotBlank
    private String title;
    @NotNull
    Date startDate;
    @NotNull
    Date endDate;
    @NotNull(message = "User ID must be defined")
    UUID userUUID;

}
