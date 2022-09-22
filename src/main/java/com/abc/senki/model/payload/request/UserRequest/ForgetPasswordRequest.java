package com.abc.senki.model.payload.request.UserRequest;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ForgetPasswordRequest {
    @NotEmpty
    @Email(regexp = ".+@.+\\..+",message = "Email is not valid")
    String email;
}
