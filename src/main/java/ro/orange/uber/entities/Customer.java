package ro.orange.uber.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@Table(
        name="customer",
        uniqueConstraints=
        @UniqueConstraint(columnNames={"phone_number"})
)
public class Customer {

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

    @OneToMany(mappedBy = "customer")
    private List<Trip> trips;

    @OneToMany(mappedBy = "customer")
    private List<Payment> payments;

    public String getFullName() {
        return this.lastName + ", " + this.firstName;
    }

    public Customer() {

    }

    public Customer(String firstName, String lastName, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }
}
