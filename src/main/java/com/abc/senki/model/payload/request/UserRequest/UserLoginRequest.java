package com.abc.senki.model.payload.request.UserRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequest {
    @NotNull
    @Email(regexp = ".+@.+\\..+",message = "Email is not valid")
    private String email;

    @NotNull
    private String password;

}
