package ro.orange.uber.entities;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

@Entity
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private Date paymentInitiationTimestamp;
    private Date paymentConfirmationTimestamp;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private String statusReason;

    private BigDecimal authAmount;
    private BigDecimal paidAmount;
    private BigDecimal rewardAmount;
    private Currency currency;

    @OneToOne
    @JoinColumn(name = "trip_id", referencedColumnName = "id")
    private Trip trip;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private Date dateCreated;
    private Date lastUpdated;

    public Payment() {

    }

    public Payment(Date paymentInitiationTimestamp, PaymentStatus status, BigDecimal paidAmount, Currency currency, Trip trip, Customer customer) {
        this.paymentInitiationTimestamp = paymentInitiationTimestamp;
        this.status = status;
        this.paidAmount = paidAmount;
        this.currency = currency;
        this.trip = trip;
        this.customer = customer;
        this.dateCreated = new Date();
    }
}

