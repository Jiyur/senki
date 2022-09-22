package com.abc.senki.model.payload.request.UserRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeUserInfoRequest {
    String fullName;
    @NotEmpty(message = "gender is required")
    String gender;
    String nickName;
}
