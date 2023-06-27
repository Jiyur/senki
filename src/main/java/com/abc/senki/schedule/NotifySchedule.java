package com.abc.senki.schedule;

import com.abc.senki.service.NotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NotifySchedule {
    @Autowired
    private NotifyService notifyService;
//     @Scheduled(fixedRate = 30000)
     public void scheduleFixedRateTask() {
         notifyService.pushChargeNotify();

     }
}
