package com.abc.senki.model.payload.request.ShipperRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShipperOrderMessage {
    @NotEmpty
    private  String status;
    private String note;

}
