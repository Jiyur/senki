package com.abc.senki.service;

import com.abc.senki.model.entity.OrderEntity;
import com.abc.senki.model.entity.UserEntity;
import com.paypal.api.payments.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Component
@Service
public interface OrderService  {
    void saveOrder(OrderEntity order);

    void saveOrderList(List<OrderEntity> orderList);
    OrderEntity getOrderById(UUID id);
    List<OrderEntity> getOrderByUser(UserEntity user);
    List<OrderEntity> getOrderByUser(UserEntity user, Pageable pageable);
    List<OrderEntity> getOrderByStatus(String status);
    List<OrderEntity> getOrderByUserAndStatus(UserEntity user, String status);

    List<OrderEntity> getAllOrder(Pageable pageable);

    List<OrderEntity> getOrderBySeller(UUID sellerId);
    void deleteOrderById(UUID id);
    void updateOrderStatus(UUID id, String status);

    void updateAllOrderStatus(String PayId,String status);

    List<OrderEntity> findAllBySellerId(UUID sellerId,Pageable pageable);
}
