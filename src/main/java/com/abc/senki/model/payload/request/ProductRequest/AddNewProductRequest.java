package com.abc.senki.model.payload.request.ProductRequest;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddNewProductRequest {
    @NotEmpty(message = "Name is Not Empty")
    private String name;

    private String description;

    @Min(value = 0,message = "Price must be greater than 0")
    @NotEmpty(message = "Price is Not Empty")
    private Double price;

    @NotEmpty(message = "Inventory is Not Empty")
    @Min(value = 1,message = "Inventory must be greater than 0")
    @Max(value = 1000, message = "Inventory must be less than 1000")
    private int stock;

    private String cate_id;
}
