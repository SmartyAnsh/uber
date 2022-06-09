package ro.orange.uber.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;

@Entity
@Data
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "start_location_id")
    private Location startLocation;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "end_location_id")
    private Location endLocation;

    private Date startTimestamp;
    private Date endTimestamp;

    private Date driverAssignedTimestamp;

    @Enumerated(EnumType.STRING)
    private TripStatus status;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    private BigDecimal fare;
    private Currency currency;

    //20% payment-auth amount
    private BigDecimal paymentAuthFareRate = new BigDecimal("0.2");

    @OneToOne(mappedBy = "trip")
    private Payment payment;

    @OneToOne(mappedBy = "trip")
    private DriverRating rating;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public boolean isDriverAssigned() {
        return this.status.equals(TripStatus.DRIVER_ASSIGNED) ? true : false;
    }

    public boolean isTripStarted() {
        return this.status.equals(TripStatus.STARTED) ? true : false;
    }

    public boolean isTripFinished() {
        return this.status.equals(TripStatus.FINISHED) ? true : false;
    }

    public boolean isTripCancelled() {
        return this.status.equals(TripStatus.CANCELLED) ? true : false;
    }

    public boolean isStartedToday() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = this.getStartTimestamp().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return today.equals(startDate) ? true: false;
    }

    public long getTripTimeInSeconds() {
        return (long)(endTimestamp.getTime() - startTimestamp.getTime())/1000;
    }

    public Trip() {

    }

    public Trip(Location startLocation, Location endLocation, TripStatus status, BigDecimal fare, Currency currency, Customer customer) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.status = status;
        this.fare = fare;
        this.currency = currency;
        this.customer = customer;
    }
}

