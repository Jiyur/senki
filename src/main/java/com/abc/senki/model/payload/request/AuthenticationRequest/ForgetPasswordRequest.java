package com.abc.senki.model.payload.request.AuthenticationRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ForgetPasswordRequest {
    @NotEmpty
    @Email(regexp = ".+@.+\\..+",message = "Email is not valid")
    private String email;
}
