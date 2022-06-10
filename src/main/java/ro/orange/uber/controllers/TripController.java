package ro.orange.uber.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.orange.uber.controllers.model.request.AcquireTripRequestModel;
import ro.orange.uber.controllers.model.request.PaymentCallbackRequestModel;
import ro.orange.uber.controllers.model.request.StartTripRequestModel;
import ro.orange.uber.controllers.model.request.TerminateTripRequestModel;
import ro.orange.uber.controllers.model.response.TripDetailsResponseModel;
import ro.orange.uber.services.TripService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/trip")
public class TripController {

    private TripService tripService;

    @Autowired
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<TripDetailsResponseModel> getTripDetails(@PathVariable("id") Long tripId) {
        TripDetailsResponseModel responseModel = tripService.getTripDetails(tripId);
        if (null != responseModel) {
            return new ResponseEntity<>(responseModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/acquire")
    public ResponseEntity<TripDetailsResponseModel> acquireTrip(@Valid @RequestBody AcquireTripRequestModel requestModel) {
        boolean isTripAcquired = tripService.acquireTrip(requestModel);
        ResponseEntity<TripDetailsResponseModel> response;
        if (isTripAcquired) {
            response = new ResponseEntity<>(tripService.getTripDetails(requestModel.getTripId()), HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        return response;
    }

    @PostMapping("/start")
    public ResponseEntity<TripDetailsResponseModel> startTrip(@Valid @RequestBody StartTripRequestModel requestModel) {
        boolean isTripStarted = tripService.startTrip(requestModel);
        ResponseEntity<TripDetailsResponseModel> response;
        if (isTripStarted) {
            response = new ResponseEntity<>(tripService.getTripDetails(requestModel.getTripId()), HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        return response;
    }

    @PostMapping("/terminate")
    public ResponseEntity terminateTrip(@Valid @RequestBody TerminateTripRequestModel requestModel) {
        try {
            tripService.terminateTrip(requestModel);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/paymentCallback")
    public ResponseEntity<Boolean> receivePaymentCallback(@Valid @RequestBody PaymentCallbackRequestModel requestModel) {
        tripService.processPaymentCallback(requestModel);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

}
