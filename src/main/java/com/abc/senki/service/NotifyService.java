package com.abc.senki.service;

import com.abc.senki.model.entity.UserEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public interface NotifyService {
    void pushChargeNotify();

}
