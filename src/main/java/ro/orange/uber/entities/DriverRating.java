package ro.orange.uber.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
public class DriverRating {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private int score;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @NotNull
    @OneToOne
    @JoinColumn(name = "trip_id", referencedColumnName = "id")
    private Trip trip;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @NotNull
    private Date dateCreated;

    public DriverRating() {

    }

    public DriverRating(int score, Customer customer, Trip trip, Driver driver) {
        this.score = score;
        this.customer = customer;
        this.trip = trip;
        this.driver = driver;
        this.dateCreated = new Date();
    }
}