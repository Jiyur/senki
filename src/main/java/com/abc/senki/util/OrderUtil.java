package com.abc.senki.util;

import com.abc.senki.model.entity.OrderAddress;
import com.abc.senki.model.entity.OrderDetailEntity;
import com.abc.senki.model.entity.OrderEntity;
import com.abc.senki.model.entity.UserEntity;
import com.abc.senki.model.payload.request.OrderRequest.CartItemList;
import com.abc.senki.model.payload.request.OrderRequest.CartItemRequest;
import org.aspectj.weaver.ast.Or;

import java.util.*;

public class OrderUtil {
    public static void setAddress(OrderEntity order, UserEntity user){
        OrderAddress address=new OrderAddress();
        address.setDistrict(user.getAddress().getDistrict());
        address.setProvince(user.getAddress().getProvince());
        address.setCommune(user.getAddress().getCommune());
        address.setInfo(
                user.getAddress().getFullName(),
                user.getAddress().getCompanyName(), user.getAddress().getPhoneNumber(),
                user.getAddress().getProvince(),
                user.getAddress().getDistrict(),
                user.getAddress().getCommune(),
                user.getAddress().getAddressDetail()
        );
        order.setAddress(address);
    }
    public static void cartProcess(OrderEntity order, List<CartItemList> listCart){
        List<OrderDetailEntity> orderDetailList = new ArrayList<>();
        for (CartItemList cartItem:listCart)
        {
            OrderDetailEntity orderDetail=new OrderDetailEntity();
            orderDetail.setInfo(order,
                    cartItem.getProductName(),
                    cartItem.getProductId(),
                    cartItem.getProductImage(),
                    cartItem.getQuantity(),
                    cartItem.getPrice());
            orderDetailList.add(orderDetail);
            order.setTotal(order.getTotal()+cartItem.getPrice()*cartItem.getQuantity());
        }
        //Set item and delete cart
        order.setOrderDetails(orderDetailList);
    }
    public static void cartProcessV2(OrderEntity order, List<CartItemRequest> listCart){
        List<OrderDetailEntity> orderDetailList = new ArrayList<>();
        for (CartItemRequest cartItem:listCart)
        {
            OrderDetailEntity orderDetail=new OrderDetailEntity();
            orderDetail.setInfo(order,
                    cartItem.getProductName(),
                    cartItem.getProductId(),
                    cartItem.getProductImage(),
                    cartItem.getQuantity(),
                    cartItem.getPrice());
            orderDetailList.add(orderDetail);
            order.setTotal(order.getTotal()+cartItem.getPrice()*cartItem.getQuantity());
        }
        //Set item and delete cart
        order.setOrderDetails(orderDetailList);
    }
    public static List<OrderEntity> handleCart(List<CartItemRequest> cartList,UserEntity user,String payId){
        Set<UUID> sellerSet=new HashSet<>();
        for (CartItemRequest item:cartList) {
            sellerSet.add(item.getSellerId());
        }
        List<OrderEntity> orderList=new ArrayList<>();
        //Process seller items
        for (UUID seller:sellerSet
        ) {
            //Init order and set order address
            OrderEntity order=new OrderEntity(user);
            OrderUtil.setAddress(order,user);
            //Set pay id for group order
            order.setPayId(payId);
            order.setSellerId(seller);
            //Pick apporiate items list for seller
            List<CartItemRequest> apporiateList=new ArrayList<>();
            for (CartItemRequest item:cartList
            ) {
                if(seller.equals(item.getSellerId())){
                    apporiateList.add(item);
                }
            }
            OrderUtil.cartProcessV2(order,apporiateList);
            order.setTotal(order.getTotal()+order.getShipFee());
            orderList.add(order);

        }
        return orderList;

    }
}
