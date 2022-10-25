package com.abc.senki.model.payload.request.CartRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddCartItemRequest {
    @NotEmpty(message = "Id can't empty")
    private String productId;

    @NotNull(message = "Quantity can't empty")
    @Min(value = 1, message = "Quantity must bigger than 1")
    private int quantity;

    private String attributeValueId;
}
