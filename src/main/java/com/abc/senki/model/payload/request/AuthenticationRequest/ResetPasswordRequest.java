package com.abc.senki.model.payload.request.AuthenticationRequest;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {
    @NotEmpty(message = "Thiếu password")
    @Size(min = 8,max = 32,message = "Password phải từ 8 đến 32 ký tự, bao gồm chữ và số")
    private String password;
    private String confirmPassword;

    private String token;
}
