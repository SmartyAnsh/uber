package ro.orange.uber.controllers.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateTripRequestModel {

    @NotNull
    private long customerId;

    @NotNull
    private String startLocation;

    @NotNull
    private String endLocation;

}
