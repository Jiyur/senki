package com.abc.senki.service;

import com.abc.senki.model.entity.OrderEntity;
import com.abc.senki.model.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Component
@Service
public interface OrderService  {
    void saveOrder(OrderEntity order);
    OrderEntity getOrderById(UUID id);
    List<OrderEntity> getOrderByUser(UserEntity user);
    List<OrderEntity> getOrderByUser(UserEntity user, Pageable pageable);
    List<OrderEntity> getOrderByStatus(String status);
    List<OrderEntity> getOrderByUserAndStatus(UserEntity user, String status);
    void deleteOrderById(UUID id);
    void updateOrderStatus(UUID id, String status);
}
