package com.abc.senki.service.implement;

import com.abc.senki.model.entity.*;
import com.abc.senki.repositories.OrderRepository;
import com.abc.senki.service.CartService;
import com.abc.senki.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CartService cartService;

    @Override
    public OrderEntity saveOrder(OrderEntity order, CartEntity cart) {
        //Init new order detail list
        List<OrderDetailEntity> orderDetailList = new ArrayList<>();
        for (CartItemEntity cartItem:cart.getCartItems())
        {
            OrderDetailEntity orderDetail=new OrderDetailEntity();
            orderDetail.setInfo(order,
                    cartItem.getProduct(),
                    cartItem.getAttributeValue(),
                    cartItem.getQuantity(),
                    cartItem.getProduct().getPrice());
            orderDetailList.add(orderDetail);
            order.setTotal(order.getTotal()+cartItem.getProduct().getPrice()*cartItem.getQuantity());
        }
        //Set item and delete cart
        order.setOrderDetails(orderDetailList);

        order.setTotal(order.getTotal()+order.getShipFee());
        cartService.deleteItemsByCart(cart);
        return orderRepository.save(order);
    }

    @Override
    public OrderEntity getOrderById(int id) {
        return null;
    }

    @Override
    public List<OrderEntity> getOrderByUser(UserEntity user) {
        return null;
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
    public void deleteOrderById(int id) {

    }

    @Override
    public void updateOrderStatus(int id, String status) {

    }
}
