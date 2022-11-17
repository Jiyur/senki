package com.abc.senki.model.payload.request.OrderRequest;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemList {
    @NotEmpty
    private UUID productId;

    @NotEmpty
    private String productName;

    private String productImage;
    @NotEmpty
    private int quantity;
    @NotEmpty
    private Double price;

}
