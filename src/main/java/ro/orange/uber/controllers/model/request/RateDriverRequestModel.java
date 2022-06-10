package ro.orange.uber.controllers.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RateDriverRequestModel {

    @NotNull
    private long customerId;

    @NotNull
    private long driverId;

    @NotNull
    private long tripId;

    @NotNull
    private int score;

}
