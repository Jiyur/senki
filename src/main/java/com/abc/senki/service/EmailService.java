package com.abc.senki.service;

import com.abc.senki.model.entity.UserEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public interface EmailService {
    void sendForgetPasswordMessage(String host,UserEntity user);
    void sendGridEmail(String host, UserEntity user);

}

