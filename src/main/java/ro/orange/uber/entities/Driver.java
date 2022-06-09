package ro.orange.uber.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Entity
@Data
@Table(
        name="driver",
        uniqueConstraints=
        @UniqueConstraint(columnNames={"phone_number"})
)
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToMany(mappedBy = "driver")
    List<Vehicle> vehicles;

    @OneToMany(mappedBy = "driver")
    List<Trip> trips;

    @OneToMany(mappedBy = "driver")
    List<DriverRating> ratings;

    public Vehicle getActiveVehicle() {
        Optional<Vehicle> vehicle = vehicles.stream().
                filter(v -> v.getIsActive()).
                findFirst();

        return vehicle.isPresent() ? vehicle.get() : null;
    }

    public Driver() {

    }

    public Driver(String firstName, String lastName, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }
}
