package com.abc.senki.model.payload.request.OrderRequest;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Data
public class CartItemRequest {
    @NotEmpty
    private UUID productId;

    @NotEmpty
    private String productName;

    private String productImage;
    @NotEmpty
    private int quantity;
    @NotEmpty
    private Double price;
    @NotEmpty
    private UUID sellerId;
}
