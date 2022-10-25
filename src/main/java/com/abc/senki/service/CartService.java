package com.abc.senki.service;

import com.abc.senki.model.entity.CartEntity;
import com.abc.senki.model.entity.CartItemEntity;
import com.abc.senki.model.entity.ProductEntity;
import com.abc.senki.model.entity.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public interface CartService {
    CartEntity saveCart(CartEntity cart);

    CartEntity getCartByUser(UserEntity user);

    List<CartItemEntity> getCartItem(CartEntity cart);

    void deleteCartById(int id);

    int calCartTotal(CartEntity cart);

    CartItemEntity saveCartItem(CartItemEntity cartItem);

    CartItemEntity getCartItemByPidAndCid(ProductEntity id, CartEntity cart);

    void deleteCartItem(int id);

    CartItemEntity getCartItemById(int id);

    CartEntity getCartByUid(UserEntity uid, Boolean status);
}
