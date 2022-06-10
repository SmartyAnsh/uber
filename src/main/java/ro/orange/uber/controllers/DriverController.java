package ro.orange.uber.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.orange.uber.controllers.model.request.CreateDriverRequestModel;
import ro.orange.uber.controllers.model.request.RegisterVehicleRequestModel;
import ro.orange.uber.controllers.model.response.*;
import ro.orange.uber.entities.Driver;
import ro.orange.uber.entities.Vehicle;
import ro.orange.uber.services.DriverService;
import ro.orange.uber.services.TripService;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/driver")
public class DriverController {

    private DriverService driverService;
    private TripService tripService;

    @Autowired
    public DriverController(DriverService driverService, TripService tripService) {
        this.driverService = driverService;
        this.tripService = tripService;
    }

    @PostMapping("/register")
    public ResponseEntity<CreateDriverResponseModel> createDriver(@Valid @RequestBody CreateDriverRequestModel requestModel) {
        Driver driver = new Driver();
        BeanUtils.copyProperties(requestModel, driver);
        driverService.createDriver(driver);
        return new ResponseEntity<>(new CreateDriverResponseModel(driver.getId()), HttpStatus.OK);
    }

    @PostMapping("/registerVehicle")
    public ResponseEntity<RegisterVehicleResponseModel> registerVehicle(@Valid @RequestBody RegisterVehicleRequestModel requestModel) {
        Vehicle vehicle = driverService.registerVehicle(requestModel);
        return new ResponseEntity<>(new RegisterVehicleResponseModel(vehicle.getId()), HttpStatus.OK);
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<DriverProfileResponseModel> getDriverProfile(@PathVariable("id") Long driverId) {
        try {
            DriverProfileResponseModel responseModel = driverService.getDriverProfile(driverId);
            return new ResponseEntity<>(responseModel, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/trips/{id}")
    public ResponseEntity<List<TripDetailsResponseModel>> getTrips(@PathVariable("id") Long driverId) {
        List<TripDetailsResponseModel> responseModel = driverService.getDriverTrips(driverId);
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }

    @GetMapping("/payments/{id}")
    public ResponseEntity<List<TripPaymentDetailsResponseModel>> getPayments(@PathVariable("id") Long driverId) {
        List<TripPaymentDetailsResponseModel> responseModel = driverService.getDriverTripPayments(driverId);
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }


}
