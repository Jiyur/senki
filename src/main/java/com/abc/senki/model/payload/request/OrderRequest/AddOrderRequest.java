package com.abc.senki.model.payload.request.OrderRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddOrderRequest {
    @NotEmpty
    private String paymentMethod;

}
