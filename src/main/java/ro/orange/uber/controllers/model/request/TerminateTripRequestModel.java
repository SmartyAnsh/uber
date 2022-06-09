package ro.orange.uber.controllers.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TerminateTripRequestModel {

    @NotNull
    private long driverId;

    @NotNull
    private long tripId;

}
