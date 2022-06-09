package ro.orange.uber.controllers.model.response;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import ro.orange.uber.entities.Customer;
import ro.orange.uber.entities.Payment;
import ro.orange.uber.entities.Trip;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TripPaymentDetailsResponseModel {
    private BigDecimal paymentAmount;
    private BigDecimal rewardAmount;
    private String paymentStatus;
    private Date initiationTimestamp;
    private Date confirmationTimestamp;

    public TripPaymentDetailsResponseModel bind(Payment payment) {
        this.paymentAmount = payment.getPaidAmount();
        this.rewardAmount = payment.getRewardAmount();
        this.paymentStatus = payment.getStatus().toString();
        this.initiationTimestamp = payment.getPaymentInitiationTimestamp();
        this.confirmationTimestamp = payment.getPaymentConfirmationTimestamp();

        return this;
    }

}
