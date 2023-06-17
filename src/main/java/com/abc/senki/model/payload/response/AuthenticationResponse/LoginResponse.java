package com.abc.senki.model.payload.response.AuthenticationResponse;

import com.abc.senki.model.entity.UserEntity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Data
public class LoginResponse {
    private UserEntity userEntity;

}
