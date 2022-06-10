package ro.orange.uber.controllers.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class PaymentCallbackRequestModel {

    @NotNull
    private BigDecimal amount;

    @NotNull
    private long tripId;

    @NotNull
    private long driverId;

    @NotNull
    private long customerId;

    @NotNull
    private String paymentStatus;

    private String statusReason;

    public PaymentCallbackRequestModel() {

    }

    @Override
    public String toString() {
        return String.format("TripId: %s, DriverId: %s, CustomerId: %s, Amount %s, Status %s",
                tripId, driverId, customerId, amount.toPlainString(), paymentStatus);
    }

}
