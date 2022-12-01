package com.abc.senki.service.implement;

import com.abc.senki.model.entity.*;
import com.abc.senki.repositories.OrderRepository;
import com.abc.senki.service.CartService;
import com.abc.senki.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CartService cartService;

    @Override
    public void saveOrder(OrderEntity order) {
        //Init new order detail list
        orderRepository.save(order);
    }

    @Override
    public OrderEntity getOrderById(UUID id) {
        return null;
    }

    @Override
    public List<OrderEntity> getOrderByUser(UserEntity user) {
        return orderRepository.getThing(user.getId()).stream().toList();
    }

    @Override
    public List<OrderEntity> getOrderByStatus(String status) {
        return null;
    }

    @Override
    public List<OrderEntity> getOrderByUserAndStatus(UserEntity user, String status) {
        return null;
    }

    @Override
    public void deleteOrderById(UUID id) {
        orderRepository.deleteById(id);
    }

    @Override
    public void updateOrderStatus(UUID id, String status) {
        OrderEntity order = orderRepository.findById(id).orElse(null);
        assert order != null;
        order.setStatus(status);
        orderRepository.save(order);
    }
}
