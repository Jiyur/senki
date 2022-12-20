package com.abc.senki.service;

import com.abc.senki.model.entity.UserEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public interface EmailService {
    public void sendForgetPasswordMessage(String host,UserEntity user);

}

