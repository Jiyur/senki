package com.abc.senki.repositories;

import com.abc.senki.model.entity.OrderEntity;
import com.abc.senki.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity,UUID> {
    Optional<OrderEntity> findById(UUID id);
    Optional<OrderEntity> findByUserAndStatus(UserEntity user, String status);

}
