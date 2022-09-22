package com.abc.senki.service;

import com.abc.senki.model.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public interface EmailService {
    public void sendForgetPasswordMessage(UserEntity user);

}

