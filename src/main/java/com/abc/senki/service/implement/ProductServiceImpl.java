package com.abc.senki.service.implement;

import com.abc.senki.model.entity.ImageProductEntity;
import com.abc.senki.model.entity.ProductEntity;
import com.abc.senki.repositories.ImageProductRepository;
import com.abc.senki.repositories.ProductRepository;
import com.abc.senki.repositories.spec.ProductSpecification;
import com.abc.senki.repositories.spec.SearchCriteria;
import com.abc.senki.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productsRepository;
    private final ImageProductRepository imageRepository;
    private static final String PRICE_ASC = "product:price_up";
    private static final String PRICE_DESC = "product:price_down";

    @Override
    public List<ProductEntity> findPaginated(Pageable page, Double minPrice, Double maxPrice) {
        Page<ProductEntity> pageResult = productsRepository.findAllProduct(page, minPrice, maxPrice);
        return pageResult.stream().toList();
    }

    @Override
    public ProductEntity saveProduct(ProductEntity product) {
        return productsRepository.save(product);
    }

    @Override
    public void deleteProductById(String id) {
        productsRepository.deleteById(UUID.fromString(id));
    }

    @Override
    public ProductEntity findById(String id) {
        return productsRepository.findById(UUID.fromString(id)).orElse(null);
    }

    @Override
    public List<ProductEntity> findAllByParent(String id, Pageable page,Double minPrice, Double maxPrice) {
        Page<ProductEntity> pageResult = productsRepository.findAllProductByParentId(UUID.fromString(id),page,minPrice,maxPrice);
        return  pageResult.stream().toList();
    }


    @Override
    public List<ProductEntity> listAll(String key, Pageable page, Double minPrice, Double maxPrice) {
        Page<ProductEntity> pageResult = productsRepository.search(key,page,minPrice,maxPrice);
        return pageResult.stream().toList();

    }

    @Override
    public void saveListImageProduct(List<String> listUrl, ProductEntity product) {
        for (String url : listUrl){
            ImageProductEntity imageProductEntity = new ImageProductEntity();
            imageProductEntity.setUrl(url);
            imageProductEntity.setProduct(product);
            imageRepository.save(imageProductEntity);}}

    @Override
    public void deleteListImgProduct(ProductEntity product) {
        List<ImageProductEntity> imageProductEntityList = imageRepository.findByProduct(product);
        imageRepository.deleteAll(imageProductEntityList);
    }


    public Pageable createPageRequest(int pageNo, int pageSize, String sort) {
        return switch (sort) {
            case PRICE_ASC -> PageRequest.of(pageNo, pageSize, Sort.by("product_price").ascending());
            case PRICE_DESC -> PageRequest.of(pageNo, pageSize, Sort.by("product_price").descending());
            default -> PageRequest.of(pageNo, pageSize, Sort.by(sort).descending());
        };
    }

}
