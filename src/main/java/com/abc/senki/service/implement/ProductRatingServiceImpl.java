package com.abc.senki.service.implement;

import com.abc.senki.model.entity.ProductRatingEntity;
import com.abc.senki.repositories.ProductRatingRepository;
import com.abc.senki.service.ProductRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
@Service
@RequiredArgsConstructor
public class ProductRatingServiceImpl implements ProductRatingService {

    @Autowired
    private ProductRatingRepository productRatingRepository;

    @Override
    public void saveRating(ProductRatingEntity productRating) {
        productRatingRepository.save(productRating);
    }

    @Override
    public List<ProductRatingEntity> getRatingByProductId(String productId) {
        return productRatingRepository.findByProductId(UUID.fromString(productId));
    }

    @Override
    public List<ProductRatingEntity> getRatingByUserId(UUID userId) {
        return productRatingRepository.findByUserId(userId);
    }
}
