package ro.orange.uber.controllers.model.response;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import ro.orange.uber.entities.Vehicle;

import java.util.Date;

@Data
public class VehicleResponseModel {

    private String name;
    private String color;
    private String registrationNumber;
    private Date registerTimestamp;

    private Boolean isActive;

    public VehicleResponseModel bind(Vehicle vehicle) {
        BeanUtils.copyProperties(vehicle, this);
        return this;
    }

}
