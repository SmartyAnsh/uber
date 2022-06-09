package ro.orange.uber.controllers.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RegisterVehicleRequestModel {

    @NotNull
    private long driverId;

    @NotNull
    private String name;

    @NotNull
    private String color;

    @NotNull
    private String registrationNumber;

}
