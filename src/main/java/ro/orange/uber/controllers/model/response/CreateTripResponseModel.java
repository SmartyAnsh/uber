package ro.orange.uber.controllers.model.response;

import lombok.Data;

@Data
public class CreateTripResponseModel {

    private long tripId;

    public CreateTripResponseModel() {

    }

    public CreateTripResponseModel(long tripId) {
        this.tripId = tripId;
    }
}
