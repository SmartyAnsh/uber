package ro.orange.uber.controllers.model.response;

import lombok.Data;

@Data
public class RegisterVehicleResponseModel {

    private long vehicleId;

    public RegisterVehicleResponseModel() {

    }

    public RegisterVehicleResponseModel(long vehicleId) {
        this.vehicleId = vehicleId;
    }

}
