package com.abc.senki;

import com.abc.senki.model.entity.ProductEntity;
import com.abc.senki.model.entity.UserEntity;
import com.abc.senki.model.payload.RevenueDTO;
import com.abc.senki.repositories.ProductRepository;
import com.abc.senki.repositories.StaticRepository;
import com.abc.senki.repositories.UserRepository;
import com.abc.senki.repositories.spec.ProductSpecification;
import com.abc.senki.repositories.spec.SearchCriteria;
import com.abc.senki.service.NotifyService;
import com.abc.senki.util.DataUtil;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static com.abc.senki.common.SortingEnum.*;

@SpringBootTest
class SenkiApplicationTests {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    StaticRepository staticRepository;
    @Autowired
        UserRepository userRepository;

    @Autowired
    NotifyService notifyService;
    private static final String PRICE_ASC = "product:price_up";
    private static final String PRICE_DESC = "product:price_down";
    @Test
    void contextLoads() {
    }

    @Test
    void test() {
//        ProductSpecification spec1=new ProductSpecification(new SearchCriteria("price",">",0));
//        ProductSpecification spec2=new ProductSpecification(new SearchCriteria("price","<",1000000));
//        Pageable page =createPageRequest(0, 10, price_asc.getSort());
//        Page<ProductEntity> list=productRepository.findAll(spec1.and(spec2),page);
//        for(ProductEntity productEntity:list){
//            System.out.println(productEntity.getName());
//        }
        List<RevenueDTO> list=staticRepository.findAllBySeller(UUID.fromString("9e049cc6-8e18-4648-ad2d-720f173da2e5"));
        for (RevenueDTO item:list
             ) {
            System.out.println(item.getTotalOrder());
        }



    }
    @Test
    void testB(){
        List<UserEntity> sellerEndLicense=userRepository.getAllSellerNearEndLicense();
        for (UserEntity item:sellerEndLicense
             ) {
            System.out.println(item.getEmail());
        }
    }
    @Test
    void getA()  {
        notifyService.pushChargeNotify();
    }
    public Pageable createPageRequest(int pageNo, int pageSize, String sort) {
        return switch (sort) {
            case PRICE_ASC -> PageRequest.of(pageNo, pageSize, Sort.by("price").ascending());
            case PRICE_DESC -> PageRequest.of(pageNo, pageSize, Sort.by("price").descending());
            default -> PageRequest.of(pageNo, pageSize, Sort.by(sort).descending());
        };
    }

}
