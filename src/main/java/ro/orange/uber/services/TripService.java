package ro.orange.uber.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.orange.uber.controllers.model.request.AcquireTripRequestModel;
import ro.orange.uber.controllers.model.request.StartTripRequestModel;
import ro.orange.uber.controllers.model.request.TerminateTripRequestModel;
import ro.orange.uber.controllers.model.response.TripDetailsResponseModel;
import ro.orange.uber.entities.Driver;
import ro.orange.uber.entities.Trip;
import ro.orange.uber.entities.TripStatus;
import ro.orange.uber.repositories.DriverRepository;
import ro.orange.uber.repositories.TripRepository;

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
    private PaymentService paymentService;

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
        } catch(Exception e) {
            log.error("Exception occurred while fetching the trip");
        }
        return isTripAcquired;
    }

    public boolean startTrip(StartTripRequestModel requestModel) {
        boolean isTripStarted = false;
        try {
            Trip trip = getTripWithLock(requestModel.getTripId());
            if (null != trip && !trip.isDriverAssigned()) {
                trip.setStatus(TripStatus.STARTED);
                trip.setStartTimestamp(new Date());
                tripRepository.save(trip);

                //create payment auth transaction
                isTripStarted = true;
            }
        } catch(Exception e) {
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
            } else {
                throw new Exception("Trip already terminated");
            }
        } else {
            throw new Exception("Trip don't belongs to the driver");
        }

        return true;
    }

}
