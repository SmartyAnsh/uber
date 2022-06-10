package ro.orange.uber.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ro.orange.uber.controllers.model.request.*;
import ro.orange.uber.controllers.model.response.TripDetailsResponseModel;
import ro.orange.uber.entities.*;
import ro.orange.uber.repositories.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private DriverRatingRepository driverRatingRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${UBER_PAYMENT_API}")
    private String UBER_PAYMENT_API;

    public Trip getTrip(long tripId) {
        Optional<Trip> trip = tripRepository.findById(tripId);
        return trip.isPresent() ? trip.get() : null;
    }

    public Trip getTripWithLock(long tripId) throws PessimisticLockingFailureException {
        Optional<Trip> trip = tripRepository.findByIdWithPessimisticLock(tripId);
        return trip.isPresent() ? trip.get() : null;
    }

    public Driver getDriver(long driverId) {
        Optional<Driver> driver = driverRepository.findById(driverId);
        return driver.isPresent() ? driver.get() : null;
    }

    public TripDetailsResponseModel getTripDetails(long tripId) {
        Trip trip = getTrip(tripId);
        if (null != trip) {
            return new TripDetailsResponseModel().bind(trip);
        } else {
            return null;
        }
    }

    public boolean acquireTrip(AcquireTripRequestModel requestModel) {
        boolean isTripAcquired = false;
        try {
            Trip trip = getTripWithLock(requestModel.getTripId());
            if (null != trip && !trip.isDriverAssigned()) {
                Driver driver = getDriver(requestModel.getDriverId());
                trip.setDriver(driver);
                trip.setVehicle(driver.getActiveVehicle());
                trip.setDriverAssignedTimestamp(new Date());
                trip.setStatus(TripStatus.DRIVER_ASSIGNED);
                tripRepository.save(trip);
                isTripAcquired = true;
            }
        } catch (Exception e) {
            log.error("Exception occurred while fetching the trip");
        }
        return isTripAcquired;
    }

    public boolean startTrip(StartTripRequestModel requestModel) {
        boolean isTripStarted = false;
        try {
            Trip trip = getTripWithLock(requestModel.getTripId());
            if (null != trip && trip.isDriverAssigned()) {
                trip.setStatus(TripStatus.STARTED);
                trip.setStartTimestamp(new Date());
                tripRepository.save(trip);

                //create payment auth transaction
                Payment payment = new Payment();
                payment.setAuthAmount(trip.getFare().multiply(trip.getPaymentAuthFareRate()));
                payment.setPaidAmount(trip.getFare());
                payment.setCurrency(Currency.getInstance("RON"));
                payment.setStatus(PaymentStatus.CREATED);
                payment.setTrip(trip);
                payment.setCustomer(trip.getCustomer());

                payment = paymentRepository.save(payment);

                try {
                    PaymentRequestModel paymentRequestModel = new PaymentRequestModel(payment.getAuthAmount(), trip.getId(), trip.getDriver().getId(), trip.getCustomer().getId());
                    restTemplate.postForObject(UBER_PAYMENT_API + "/api/payment/authorize", paymentRequestModel, Boolean.class);
                    payment.setStatus(PaymentStatus.STATUS_AUTHORIZED_PENDING);
                    payment.setLastUpdated(new Date());
                    payment = paymentRepository.save(payment);
                } catch (Exception e) {
                    log.error("Error in Uber Payment API while payment authorization");
                }

                isTripStarted = true;
            }
        } catch (Exception e) {
            log.error("Exception occurred while starting the trip");
        }
        return isTripStarted;
    }

    public boolean terminateTrip(TerminateTripRequestModel requestModel) throws Exception {
        Trip trip = null;
        try {
            trip = getTripWithLock(requestModel.getTripId());
        } catch (Exception e) {
            log.error("Exception occurred while fetching the trip");
        }
        if (trip.getDriver().getId() == requestModel.getDriverId()) {
            if (trip.isTripStarted()) {
                trip.setStatus(TripStatus.FINISHED);
                trip.setEndTimestamp(new Date());
                tripRepository.save(trip);

                //create payment confirmation transaction
                try {
                    Payment payment = trip.getPayment();
                    PaymentRequestModel paymentRequestModel = new PaymentRequestModel(payment.getPaidAmount(), trip.getId(), trip.getDriver().getId(), trip.getCustomer().getId());
                    restTemplate.postForObject(UBER_PAYMENT_API + "/api/payment/confirm", paymentRequestModel, Boolean.class);
                    payment.setStatus(PaymentStatus.STATUS_CONFIRMED_PENDING);
                    payment.setLastUpdated(new Date());
                    payment = paymentRepository.save(payment);
                } catch (Exception e) {
                    log.error("Error in Uber Payment API while payment confirmation");
                }
            } else {
                throw new Exception("Trip already terminated");
            }
        } else {
            throw new Exception("Trip don't belongs to the driver");
        }

        return true;
    }

    public void processPaymentCallback(PaymentCallbackRequestModel requestModel) {
        log.info("Processing Payment callback from Uber Payment API with request " + requestModel.toString());
        Trip trip = getTripWithLock(requestModel.getTripId());

        Payment tripPayment = trip.getPayment();

        tripPayment.setStatus(PaymentStatus.valueOf(requestModel.getPaymentStatus()));
        tripPayment.setStatusReason(requestModel.getStatusReason());
        if ("STATUS_AUTHORIZED".equals(requestModel.getPaymentStatus())) {
            tripPayment.setPaymentInitiationTimestamp(new Date());
        } else if ("STATUS_CONFIRMED".equals(requestModel.getPaymentStatus())) {
            tripPayment.setPaymentConfirmationTimestamp(new Date());
        }
        tripPayment.setLastUpdated(new Date());

        paymentRepository.save(tripPayment);
    }

    public long createTrip(CreateTripRequestModel requestModel) {
        Trip trip = new Trip();
        trip.setCurrency(Currency.getInstance("RON"));
        trip.setStatus(TripStatus.CREATED);
        trip.setCustomer(customerRepository.findById(requestModel.getCustomerId()).get());
        //hard coded locations
        trip.setStartLocation(locationRepository.findByName("Henri Coandă International Airport"));
        trip.setEndLocation(locationRepository.findByName("AFI Cotroceni"));

        trip.setFare(new BigDecimal("200"));
        trip.setPaymentAuthFareRate(new BigDecimal("0.2"));
        trip = tripRepository.save(trip);
        return trip.getId();
    }

    public void rateDriver(RateDriverRequestModel requestModel) {
        Trip trip = tripRepository.findById(requestModel.getTripId()).get();
        DriverRating driverRating = new DriverRating(requestModel.getScore(), trip.getCustomer(), trip, trip.getDriver());
        driverRating = driverRatingRepository.save(driverRating);
    }

}
