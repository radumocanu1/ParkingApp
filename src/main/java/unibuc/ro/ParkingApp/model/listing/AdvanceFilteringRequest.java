package unibuc.ro.ParkingApp.model.listing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvanceFilteringRequest {
    int sector;
    Date startDate;
    Date endDate;
    int maxDailyPrice;
    int maxMonthlyPrice;
    boolean indefinitePeriod;

}
