package com.abc.senki.service;

import com.abc.senki.model.entity.CartEntity;
import com.abc.senki.model.entity.CartItemEntity;
import com.abc.senki.model.entity.ProductEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Component
public interface CartItemService {
    Optional<CartItemEntity> findByProductAndCart(ProductEntity product, CartEntity cart);
    Optional<CartItemEntity> findByIdAndCart(int id,CartEntity cart);
}
