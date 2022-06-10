package ro.orange.uber.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.orange.uber.controllers.model.request.CreateTripRequestModel;
import ro.orange.uber.controllers.model.request.RateDriverRequestModel;
import ro.orange.uber.controllers.model.response.CreateTripResponseModel;
import ro.orange.uber.services.TripService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private TripService tripService;

    @PostMapping("/trip")
    public ResponseEntity<CreateTripResponseModel> createTrip(@Valid @RequestBody CreateTripRequestModel requestModel) {
        long tripId = tripService.createTrip(requestModel);
        return new ResponseEntity<>(new CreateTripResponseModel(tripId), HttpStatus.OK);
    }

    @PostMapping("/rateDriver")
    public ResponseEntity rateDriver(@Valid @RequestBody RateDriverRequestModel requestModel) {
        tripService.rateDriver(requestModel);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
