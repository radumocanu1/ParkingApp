package unibuc.ro.ParkingApp.model.listing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DateRange {
    private Date startDate;
    private Date endDate;

}