package ro.orange.uber.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ro.orange.uber.entities.*;
import ro.orange.uber.repositories.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Currency;
import java.util.Date;

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private DriverRatingRepository driverRatingRepository;

    public void run(ApplicationArguments args) throws ParseException {

        //create 2 customer
        Customer customer1 = new Customer("John", "Smith","+40123456789");
        Customer customer2 = new Customer("Michael", "Jordan", "+40123456780");
        if (null == customerRepository.findByPhoneNumber("+40123456789")) {
            customerRepository.save(customer1);
        }
        if (null == customerRepository.findByPhoneNumber("+40123456780")) {
            customerRepository.save(customer2);
        }

        Location location1 = new Location("McDonald's, America House, Șoseaua Nicolae Titulescu 4-8, București 011141",
                44.45254985387442d, 26.082313524949832d);
        Location location2 = new Location("Henri Coandă International Airport",
                44.57532816363908d, 26.084595753831046d);
        Location location3 = new Location("AFI Cotroceni",
                44.43222989032381d, 26.05179795933162d);
        //create 3 locations
        if (null == locationRepository.findByName("McDonald's, America House, Șoseaua Nicolae Titulescu 4-8, București 011141")) {
            locationRepository.save(location1);
        }
        if (null == locationRepository.findByName("Henri Coandă International Airport")) {
            locationRepository.save(location2);
        }
        if (null == locationRepository.findByName("AFI Cotroceni")) {
            locationRepository.save(location3);
        }

        //create few drivers with vehicles

        if (null == driverRepository.findByPhoneNumber("+40123456777")) {
            Driver driver1 = new Driver("Ionut", "Popa", "+40123456777");
            driver1 = driverRepository.save(driver1);

            Vehicle vehicleDriver1 = new Vehicle("Renault Megane", "White", "B07MOP");
            vehicleDriver1 = vehicleRepository.save(vehicleDriver1);
            vehicleDriver1.setDriver(driver1);
            vehicleRepository.save(vehicleDriver1);

            //create trip1
            Trip trip1 = new Trip(location1, location2, TripStatus.CREATED, new BigDecimal("100.89"), Currency.getInstance("RON"), customer1);
            trip1 = tripRepository.save(trip1);

            //acquire trip
            trip1.setDriver(driver1);
            trip1.setVehicle(vehicleDriver1);
            trip1.setStatus(TripStatus.DRIVER_ASSIGNED);
            trip1 = tripRepository.save(trip1);

            //start trip
            trip1.setStartTimestamp(new Date());
            trip1.setStatus(TripStatus.STARTED);
            trip1 = tripRepository.save(trip1);

            Payment payment = new Payment(new Date(), PaymentStatus.STATUS_AUTHORIZED, trip1.getFare(), Currency.getInstance("RON"), trip1, customer1);
            payment.setAuthAmount(trip1.getFare().multiply(trip1.getPaymentAuthFareRate())); //20% of the trip fare
            payment.setPaymentInitiationTimestamp(new Date());
            payment = paymentRepository.save(payment);

            //finish trip
            trip1.setEndTimestamp(new Date(trip1.getStartTimestamp().getTime() + (20 * 60 * 1000))); //after 20 mins
            trip1.setStatus(TripStatus.FINISHED);
            tripRepository.save(trip1);

            payment.setStatus(PaymentStatus.STATUS_CONFIRMED);
            payment.setPaymentConfirmationTimestamp(new Date(trip1.getStartTimestamp().getTime() + (20 * 60 * 1000))); //after 20 mins
            paymentRepository.save(payment);

            //trip rating
            DriverRating rating = new DriverRating(4, customer1, trip1, driver1);
            driverRatingRepository.save(rating);
        }

        if (null == driverRepository.findByPhoneNumber("+40123000777")) {
            Driver driver2 = new Driver("Adrian", "Crisan", "+40123000777");
            driver2 = driverRepository.save(driver2);

            Vehicle vehicleDriver2 = new Vehicle("Dacia Logan", "Black", "IF10MOP");
            vehicleDriver2 = vehicleRepository.save(vehicleDriver2);
            vehicleDriver2.setDriver(driver2);
            vehicleRepository.save(vehicleDriver2);
        }
    }
}

