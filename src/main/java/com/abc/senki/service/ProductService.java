package com.abc.senki.service;

import com.abc.senki.model.entity.ProductEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Component
@Service
public interface ProductService {
    List<ProductEntity> findPaginated(Pageable page,Double minPrice, Double maxPrice);
    void saveProduct(ProductEntity product);
    void deleteProductById(String id);
    ProductEntity findById(String id);
    List<ProductEntity> findAll(Specification<ProductEntity> spec);
    List<ProductEntity> findAllByParent(String id,Pageable page,Double minPrice,Double maxPrice);
    List<ProductEntity> findAllByChild(String id,Pageable page,Double minPrice,Double maxPrice);
//    Page<ProductEntity> findAll(String key, int pageNo, int pageSize, String sort, Double minPrice, Double maxPrice);
    List<ProductEntity> listAll(String key, Pageable pageable, Double minPrice, Double maxPrice);

    void saveListImageProduct(List<String> listUrl, ProductEntity product);

    void deleteListImgProduct(ProductEntity product);

    void disableProduct(String productId);

    void enableProduct(String productId);

    List<ProductEntity> findAllBySeller(UUID sellerId, Pageable page, Double minPrice, Double maxPrice);

    List<ProductEntity> findBySellerAndKey(UUID sellerId, Pageable page, Double minPrice, Double maxPrice, String key);

}
