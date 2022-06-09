package ro.orange.uber.controllers.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateDriverRequestModel {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String phoneNumber;

}
