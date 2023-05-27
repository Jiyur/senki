package com.abc.senki.service.implement;

import com.abc.senki.model.entity.*;
import com.abc.senki.repositories.OrderRepository;
import com.abc.senki.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository;


    @Override
    public void saveOrder(OrderEntity order) {
        //Init new order detail list
        orderRepository.save(order);
    }

    @Override
    public void saveOrderList(List<OrderEntity> orderList) {
        orderRepository.saveAll(orderList);
    }

    @Override
    public OrderEntity getOrderById(UUID id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public List<OrderEntity> getOrderByUser(UserEntity user) {
        return orderRepository.getThing(user.getId()).stream().toList();
    }

    @Override
    public List<OrderEntity> getOrderByUser(UserEntity user, Pageable pageable) {
        return orderRepository.findDistinctByUser(user, pageable).stream().toList();
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
    public List<OrderEntity> getAllOrder(Pageable pageable) {
        return orderRepository.findAll(pageable).stream().toList();
    }

    @Override
    public List<OrderEntity> getOrderBySeller(UUID sellerId) {
        return null;
    }

    @Override
    public void deleteOrderById(UUID id) {
        orderRepository.deleteById(id);
    }

    @Override
    public void updateOrderStatus(UUID id, String status) {
        OrderEntity order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setStatus(status);
        }
        assert order != null;
        orderRepository.save(order);
    }

    @Override
    public void updateAllOrderStatus(String PayId, String status) {
        try
        {
            orderRepository.updateAllOrderStatus(PayId,status);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    @Override
    public List<OrderEntity> findAllBySellerId(UUID sellerId,Pageable pageable) {
        return orderRepository.findAllBySellerId(sellerId,pageable);

    }
}
