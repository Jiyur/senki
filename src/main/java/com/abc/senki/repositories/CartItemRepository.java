package com.abc.senki.repositories;

import com.abc.senki.model.entity.CartEntity;
import com.abc.senki.model.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItemEntity,Integer> {
    List<CartItemEntity> findByCart(CartEntity cart);
    void deleteAllByCart(CartEntity cart);
}
