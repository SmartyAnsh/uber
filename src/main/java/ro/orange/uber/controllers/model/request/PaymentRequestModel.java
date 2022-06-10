package ro.orange.uber.controllers.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class PaymentRequestModel {

    @NotNull
    private BigDecimal amount;

    @NotNull
    private long tripId;

    @NotNull
    private long driverId;

    @NotNull
    private long customerId;

    @Override
    public String toString() {
        return String.format("TripId: %s, DriverId: %s, CustomerId: %s, Amount %s", tripId, driverId, customerId, amount.toPlainString());
    }

    public PaymentRequestModel() {}

    public PaymentRequestModel(BigDecimal amount, long tripId, long driverId, long customerId) {
        this.amount = amount;
        this.tripId = tripId;
        this.driverId = driverId;
        this.customerId = customerId;
    }
}
