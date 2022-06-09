package ro.orange.uber.controllers.model.response;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import ro.orange.uber.entities.Customer;
import ro.orange.uber.entities.Trip;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TripDetailsResponseModel {

    private String customerFullName;
    private String customerPhoneNumber;
    private String startLocationName;
    private String endLocationName;
    private Date startTimestamp;
    private Date endTimestamp;
    private BigDecimal fare;

    private TripPaymentDetailsResponseModel paymentInfo;

    private Integer rating;
    private String vehicleInfo;

    public TripDetailsResponseModel() {

    }

    public TripDetailsResponseModel bind(Trip trip) {
        BeanUtils.copyProperties(trip, this);

        Customer customer = trip.getCustomer();

        this.customerFullName = customer.getFullName();
        this.customerPhoneNumber = customer.getPhoneNumber();
        this.startLocationName = trip.getStartLocation().getName();
        this.endLocationName = trip.getEndLocation().getName();
        if (null != trip.getPayment()) {
            this.paymentInfo = new TripPaymentDetailsResponseModel().bind(trip.getPayment());
        }
        if (null != trip.getRating()) {
            this.rating = trip.getRating().getScore();
        }

        if (null != trip.getVehicle()) {
            this.vehicleInfo = trip.getVehicle().toString();
        }

        return this;
    }

}