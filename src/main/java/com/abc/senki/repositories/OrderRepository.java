package com.abc.senki.repositories;

import com.abc.senki.model.entity.OrderEntity;
import com.abc.senki.model.entity.UserEntity;
import com.paypal.api.payments.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity,UUID> {
    Optional<OrderEntity> findById(UUID id);
    Optional<OrderEntity> findByUserAndStatus(UserEntity user, String status);
    @Query(value="select distinct * from orders o where o.user_id = ?1",nativeQuery = true)
    List<OrderEntity> getThing(UUID id);
    List<OrderEntity> findDistinctByUser(UserEntity user, Pageable pageable);

    List<OrderEntity> findAllBySellerId(UUID sellerId,Pageable pageable);

    @Modifying
    @Query(value = "update orders SET status=?2 where pay_id=?1",nativeQuery = true)
    void updateAllOrderStatus(String payId,String status);

    @Query(value = "select * from orders o " +
            "where o.address_id " +
            "in (SELECT id from order_address where province=?1)" +
            " AND o.status LIKE ?2 " +
            " AND o.shipper_id is null",nativeQuery = true)
    List<OrderEntity> findAllByProvinceAndStatus(Pageable pageable,String provinceId,String status);


}
