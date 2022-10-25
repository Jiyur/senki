package com.abc.senki.service;

import com.abc.senki.model.entity.OrderDetailEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public interface OrderDetailService {
    void saveOrderDetail(OrderDetailEntity orderDetail);

}
