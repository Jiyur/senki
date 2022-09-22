package com.abc.senki.model.payload.request.UserRequest;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@Setter
@Getter
public class ChangePasswordRequest {
    private String oldPassword;

    @NotEmpty(message = "Thiếu password")
    @Size(min = 8,max = 32,message = "Password phải từ 8 đến 32 ký tự, bao gồm chữ và số")
    private String newPassword;
    private String confirmPassword;

}
