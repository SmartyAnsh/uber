package ro.orange.uber.controllers.model.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DriverProfileResponseModel {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private double currentRatingScore;
    private BigDecimal averageTripFare;
    private List<VehicleResponseModel> vehicles;

    private DriverDailyInfo dailyInfo;

    public void setDailyInfo(int totalTripsToday, BigDecimal totalFareToday, BigDecimal averageFareToday, long totalTripsTimeTodayInSeconds) {
        this.dailyInfo = new DriverDailyInfo(totalTripsToday, totalFareToday, averageFareToday, totalTripsTimeTodayInSeconds);
    }
}

@Data
class DriverDailyInfo {

    private int totalTripsToday;
    private BigDecimal totalFareToday;
    private BigDecimal averageFareToday;
    private long totalTripsTimeTodayInSeconds;

    public DriverDailyInfo() {

    }

    public DriverDailyInfo(int totalTripsToday, BigDecimal totalFareToday, BigDecimal averageFareToday, long totalTripsTimeTodayInSeconds) {
        this.totalTripsToday = totalTripsToday;
        this.totalFareToday = totalFareToday;
        this.averageFareToday = averageFareToday;
        this.totalTripsTimeTodayInSeconds = totalTripsTimeTodayInSeconds;
    }
}

