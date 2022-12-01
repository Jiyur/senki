package com.abc.senki.repositories;

import com.abc.senki.model.entity.OrderEntity;
import com.abc.senki.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity,UUID> {
    Optional<OrderEntity> findById(UUID id);
    Optional<OrderEntity> findByUserAndStatus(UserEntity user, String status);
    @Query(value="select distinct * from orders o where o.user_id = ?1",nativeQuery = true)
    List<OrderEntity> getThing(UUID id);

}
