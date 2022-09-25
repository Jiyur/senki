package com.abc.senki.model.payload.request.UserRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePhoneRequest {
    @Min(value = 8,message = "Phone number must be at least 8 digits")
    String phone;
}
