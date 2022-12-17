package com.abc.senki.repositories;

import com.abc.senki.model.entity.ProductEntity;
import com.abc.senki.model.entity.ProductRatingEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRatingRepository extends JpaRepository<ProductRatingEntity, UUID> {
    List<ProductRatingEntity> findByProductId(UUID id);
    List<ProductRatingEntity> findByUserId(UUID id);
    List<ProductRatingEntity> findDistinctByProductId(UUID id, Pageable pageable);



}
