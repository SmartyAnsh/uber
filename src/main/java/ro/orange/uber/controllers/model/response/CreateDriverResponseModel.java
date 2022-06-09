package ro.orange.uber.controllers.model.response;

import lombok.Data;

@Data
public class CreateDriverResponseModel {

    private long driverId;

    public CreateDriverResponseModel() {

    }

    public CreateDriverResponseModel(long driverId) {
        this.driverId = driverId;
    }

}
