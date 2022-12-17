package com.abc.senki.service;

import com.abc.senki.model.entity.ProductRatingEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Component
@Service
public interface ProductRatingService {

    void saveRating(ProductRatingEntity productRating);
    List<ProductRatingEntity> getRatingByProductId(String productId);
    List<ProductRatingEntity> getRatingByProductId(String productId, Pageable pageable);

    List<ProductRatingEntity> getRatingByUserId(UUID userId);
    ProductRatingEntity getRatingById(String id);
    void deleteRating(UUID id);

}
