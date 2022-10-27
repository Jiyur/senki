package com.abc.senki.service;

import com.abc.senki.model.entity.CartEntity;
import com.abc.senki.model.entity.OrderEntity;
import com.abc.senki.model.entity.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public interface OrderService  {
    void saveOrder(OrderEntity order, CartEntity cart);
    OrderEntity getOrderById(int id);
    List<OrderEntity> getOrderByUser(UserEntity user);
    List<OrderEntity> getOrderByStatus(String status);
    List<OrderEntity> getOrderByUserAndStatus(UserEntity user, String status);
    void deleteOrderById(int id);
    void updateOrderStatus(int id, String status);
}
