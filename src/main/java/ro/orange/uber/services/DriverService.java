package ro.orange.uber.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.orange.uber.controllers.model.request.RegisterVehicleRequestModel;
import ro.orange.uber.controllers.model.response.*;
import ro.orange.uber.entities.Driver;
import ro.orange.uber.entities.DriverRating;
import ro.orange.uber.entities.Trip;
import ro.orange.uber.entities.Vehicle;
import ro.orange.uber.repositories.DriverRatingRepository;
import ro.orange.uber.repositories.DriverRepository;
import ro.orange.uber.repositories.TripRepository;
import ro.orange.uber.repositories.VehicleRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private DriverRatingRepository driverRatingRepository;

    @Autowired
    private TripRepository tripRepository;

    public Driver createDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    public Vehicle registerVehicle(RegisterVehicleRequestModel requestModel) {
        List<Vehicle> activeVehicles = vehicleRepository.findAllByDriverIdAndIsActive(requestModel.getDriverId(), true);
        activeVehicles.forEach(i -> i.setIsActive(false));
        vehicleRepository.saveAll(activeVehicles);

        Optional<Driver> driver = driverRepository.findById(requestModel.getDriverId());
        Vehicle vehicle = new Vehicle(requestModel.getName(), requestModel.getColor(), requestModel.getRegistrationNumber());
        vehicle.setDriver(driver.get());
        return vehicleRepository.save(vehicle);
    }

    public DriverProfileResponseModel getDriverProfile(Long driverId) throws NoSuchElementException {
        DriverProfileResponseModel response = new DriverProfileResponseModel();
        Optional<Driver> driver = driverRepository.findById(driverId);
        BeanUtils.copyProperties(driver.get(), response);

        List<Vehicle> vehicles = vehicleRepository.findAllByDriverId(driverId);
        List<VehicleResponseModel> vehicleResponseModels = vehicles.stream()
                .sorted(Comparator.comparing(Vehicle::getRegisterTimestamp).reversed())
                .map(i -> new VehicleResponseModel().bind(i))
                .collect(Collectors.toList());

        response.setVehicles(vehicleResponseModels);

        double currentRatingScore;
        List<DriverRating> driverRatings = driverRatingRepository.findAllByDriverId(driverId);
        if (driverRatings.size() > 0) {
            currentRatingScore = driverRatings.stream()
                    .mapToInt(i -> i.getScore()).average().getAsDouble();

            response.setCurrentRatingScore(currentRatingScore);
        }

        List<Trip> trips = tripRepository.findAllByDriverId(driverId);
        if (trips.size() > 0) {
            BigDecimal averageTripFare = trips.stream()
                    .map(Trip::getFare)
                    .reduce(BigDecimal.ZERO, (p, q) -> p.add(q))
                    .divide(BigDecimal.valueOf(trips.size()));

            response.setAverageTripFare(averageTripFare.setScale(2, RoundingMode.HALF_UP));

            //today finished trips
            List<Trip> todayTrips = trips.stream()
                    .filter(i-> i.isStartedToday() && i.isTripFinished()).collect(Collectors.toList());

            if (todayTrips.size() > 0) {
                BigDecimal totalFareToday = todayTrips.stream()
                        .map(Trip::getFare)
                        .reduce(BigDecimal.ZERO, (p, q) -> p.add(q));

                BigDecimal averageFareToday = totalFareToday.divide(BigDecimal.valueOf(todayTrips.size()));

                long totalTripsTimeTodayInSeconds = todayTrips.stream()
                        .mapToLong(Trip::getTripTimeInSeconds).sum();

                response.setDailyInfo(todayTrips.size(), totalFareToday, averageFareToday, totalTripsTimeTodayInSeconds);
            }
        }

        return response;
    }

    public List<TripDetailsResponseModel> getDriverTrips(Long driverId) {
        List<TripDetailsResponseModel> tripDetailsResponseModels = Collections.EMPTY_LIST;
        List<Trip> trips = tripRepository.findAllByDriverId(driverId);
        if (!trips.isEmpty()) {
            tripDetailsResponseModels = trips.stream()
                    .map(i -> new TripDetailsResponseModel().bind(i)).collect(Collectors.toList());
        }
        return tripDetailsResponseModels;
    }

    public List<TripPaymentDetailsResponseModel> getDriverTripPayments(Long driverId) {
        List<TripPaymentDetailsResponseModel> tripPaymentDetailsResponseModels = Collections.EMPTY_LIST;
        List<Trip> trips = tripRepository.findAllByDriverId(driverId);
        if (!trips.isEmpty()) {
            tripPaymentDetailsResponseModels = trips.stream()
                    .map(i -> null != i.getPayment() ? new TripPaymentDetailsResponseModel().bind(i.getPayment()) : null)
                    .collect(Collectors.toList());

        }
        return tripPaymentDetailsResponseModels;
    }

}
