package com.abc.senki.service.implement;

import com.abc.senki.model.entity.CartEntity;
import com.abc.senki.model.entity.CartItemEntity;
import com.abc.senki.model.entity.ProductEntity;
import com.abc.senki.model.entity.UserEntity;
import com.abc.senki.repositories.CartRepository;
import com.abc.senki.repositories.CartItemRepository;
import com.abc.senki.service.CartService;
import com.abc.senki.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    @Autowired
    final CartItemRepository cartItemRepository;
    @Autowired
    final CartRepository cartRepository;
    @Autowired
    final ProductService productService;

    @Override
    public CartEntity saveCart(CartEntity cart) {
        return cartRepository.save(cart);
    }

    @Override
    public CartEntity getCartByUser(UserEntity user) {
        return cartRepository.findByUserAndStatus(user, true).orElse(null);
    }

    @Override
    public List<CartItemEntity> getCartItem(CartEntity cart) {
        return null;
    }

    @Override
    public void deleteCartById(int id) {
        cartRepository.deleteById(id);
    }

    @Override
    public int calCartTotal(CartEntity cart) {
        return 0;
    }

    @Override
    public CartItemEntity saveCartItem(CartItemEntity cartItem) {
        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItemEntity getCartItemByPidAndCid(ProductEntity id, CartEntity cart) {
        return null;
    }

    @Override
    public void deleteCartItem(int id) {
        cartItemRepository.deleteById(id);
    }

    @Override
    public CartItemEntity getCartItemById(int id) {
        return cartItemRepository.findById(id).orElse(null);
    }

    @Override
    public CartEntity getCartByUid(UserEntity uid, Boolean status) {
        return null;
    }
}
