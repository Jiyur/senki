package com.abc.senki.model.payload.request.ProductRatingRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class AddNewRatingRequest {
    @Min(value = 1,message = "Rating can't be less than 1 star")
    @Max(value = 5,message = "Rating can't be greater than 5 star")
    private int rating;
    private String comment;


}
