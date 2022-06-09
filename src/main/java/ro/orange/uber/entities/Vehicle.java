package ro.orange.uber.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String name;

    @NotNull
    private String color;

    @NotNull
    private String registrationNumber;

    @NotNull
    private Date registerTimestamp;

    @NotNull
    private Boolean isActive;
    private Date lastUpdated;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "driver_id")
    private Driver driver;

    public Vehicle() {

    }

    public Vehicle(String name, String color, String registrationNumber) {
        this.name = name;
        this.color = color;
        this.registrationNumber = registrationNumber;
        this.registerTimestamp = new Date();
        this.isActive = true;
        this.lastUpdated = new Date();
    }

    public String toString() {
        return color + " : " + name + " : " + registrationNumber;
    }
}
