package com.abc.senki.model.payload.request.UserRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddNewUserRequest {
    @NotEmpty
    @Email(regexp = ".+@.+\\..+",message = "Email is not valid")
    private String email;
    @NotEmpty
    private String password;

}
