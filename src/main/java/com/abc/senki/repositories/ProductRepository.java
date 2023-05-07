package com.abc.senki.repositories;

import com.abc.senki.model.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.UUID;

@EnableJpaRepositories
public interface ProductRepository extends JpaRepository<ProductEntity, UUID>, JpaSpecificationExecutor<ProductEntity> {
    @Query(value = "SELECT * FROM products WHERE price BETWEEN ?1 AND ?2",
            countQuery = "SELECT count(*) FROM products" +
                    " WHERE price BETWEEN ?1 AND ?2 ",
            nativeQuery = true)
    Page<ProductEntity> findAllProduct(Pageable pageable, Double minPrice, Double maxPrice);
    @Query(value = "SELECT *" +
            " FROM products p WHERE p.cate_id IN (SELECT cate_id from categories where parent_id=?1 OR cate_id=?1) " +
            "AND p.price BETWEEN ?2 AND ?3",
            countQuery = "SELECT count(*) " +
                    "FROM products p WHERE p.cate_id IN (SELECT cate_id from categories where parent_id=?1 OR cate_id=?1) " +
                    "AND p.price BETWEEN ?2 AND ?3",
            nativeQuery = true)
    Page<ProductEntity> findAllProductByParentId(UUID id, Pageable pageable,Double minPrice,Double maxPrice);
//
    @Query(value = "SELECT * FROM products WHERE price BETWEEN ?2 AND ?3 AND (LOWER(name) LIKE %?1%" +
            " OR LOWER(description) LIKE %?1%)",
            countQuery = "SELECT count(*) FROM products" +
                    " WHERE price BETWEEN ?2 AND ?3 AND LOWER(name) LIKE %?1% OR LOWER(description) LIKE %?1%",
            nativeQuery = true)
    Page<ProductEntity> search(String name, Pageable pageable, Double minPrice, Double maxPrice);

    //Set product Status to false
    @Query(value = "UPDATE products SET status = false WHERE id = ?1", nativeQuery = true)
    void disableProduct(UUID productId);
    //Set product Status to true
    @Query(value = "UPDATE products SET status = true WHERE id = ?1", nativeQuery = true)
    void enableProduct(UUID productId);

    @Query(value = "SELECT * FROM products WHERE price BETWEEN ?2 AND ?3 AND seller_id=?1",nativeQuery = true)
    Page<ProductEntity> findAllBySeller(UUID sellerId, Pageable pageable, Double minPrice, Double maxPrice);



}
