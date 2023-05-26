package com.abc.senki.controller.seller;


import com.abc.senki.common.SortingEnum;
import com.abc.senki.handler.AuthenticationHandler;
import com.abc.senki.mapping.ProductMapping;
import com.abc.senki.model.entity.CategoryEntity;
import com.abc.senki.model.entity.ProductEntity;
import com.abc.senki.model.entity.UserEntity;
import com.abc.senki.model.payload.request.ProductRequest.AddNewProductRequest;
import com.abc.senki.model.payload.response.ErrorResponse;
import com.abc.senki.model.payload.response.SuccessResponse;
import com.abc.senki.repositories.BrandService;
import com.abc.senki.service.CategoryService;
import com.abc.senki.service.ProductService;
import com.abc.senki.util.PageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.abc.senki.common.ErrorDefinition.ERROR_TRY_AGAIN;
import static com.abc.senki.common.ErrorDefinition.SORTING_TYPE_NOT_FOUND;

@RequestMapping("/api/seller/product")
@SecurityRequirement(name = "bearerAuth")
@RestController
public class SellerProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;
    @Autowired
    AuthenticationHandler authenticationHandler;

    //Get all product by seller
    @GetMapping
    @Operation(summary = "Get all product by seller")
    public ResponseEntity<Object> listAllBySeller(HttpServletRequest request,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "20") int size,
                                                  @RequestParam(defaultValue = "product_id") SortingEnum sort,
                                                  @RequestParam(defaultValue = "0") Double minPrice,
                                                  @RequestParam(defaultValue = "10000000") Double maxPrice){
        if(!SortType().contains(sort.getSort())){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ErrorResponse.error(SORTING_TYPE_NOT_FOUND.getMessage(), HttpStatus.CONFLICT.value()));
        }
        //Create paging
        Pageable paging = PageUtil.createPageRequest(page, size, sort.getSort());
        UserEntity seller=authenticationHandler.userAuthenticate(request);
        //Put data map
        HashMap<String,Object> data=new HashMap<>();
        data.put("list",productService.findAllBySeller(seller.getId(),paging,minPrice,maxPrice));
        //Return data
        return ResponseEntity.ok(new SuccessResponse(
                HttpStatus.OK.value(),
                "Get data for seller",
                data
        ));

    }
    @GetMapping
    @Operation(summary = "Get product by seller and key")
    public ResponseEntity<Object> searchProductByKeyAndSeller(HttpServletRequest request,
                                                  @RequestParam(defaultValue = "") String key,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "20") int size,
                                                  @RequestParam(defaultValue = "product_id") SortingEnum sort,
                                                  @RequestParam(defaultValue = "0") Double minPrice,
                                                  @RequestParam(defaultValue = "10000000") Double maxPrice){
        if(!SortType().contains(sort.getSort())){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ErrorResponse.error(SORTING_TYPE_NOT_FOUND.getMessage(), HttpStatus.CONFLICT.value()));
        }
        //Create paging
        Pageable paging = PageUtil.createPageRequest(page, size, sort.getSort());
        UserEntity seller=authenticationHandler.userAuthenticate(request);
        //Put data map
        HashMap<String,Object> data=new HashMap<>();

        if(key.isEmpty()||key.isBlank()){
            data.put("list",productService.findAllBySeller(seller.getId(),paging,minPrice,maxPrice));
        }
        else{
            data.put("list",productService.findBySellerAndKey(seller.getId(),paging,minPrice,maxPrice,key));
        }
        //Return data
        return ResponseEntity.ok(new SuccessResponse(
                HttpStatus.OK.value(),
                "Get data for seller",
                data
        ));

    }
    @PostMapping("")
    @Operation(summary = "Insert product")
    public ResponseEntity<Object> insertProduct(HttpServletRequest servletRequest,
                                                @RequestBody AddNewProductRequest request){
        UserEntity seller=authenticationHandler.userAuthenticate(servletRequest);
        CategoryEntity categoryEntity = categoryService.findById(request.getCate_id());
        ProductEntity product= ProductMapping.toEntity(request,categoryEntity,seller);
        if(request.getBrand_id().length()>0){
            product.setBrand(brandService.getBrandById(Integer.parseInt(request.getBrand_id())));
        }
        try{
            productService.saveProduct(product);
            HashMap<String,Object> data=new HashMap<>();
            data.put("product_id",product.getId());
            return ResponseEntity
                    .ok(new SuccessResponse(HttpStatus.OK.value(),"Insert product successfully", data));
        }
        catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(ErrorResponse.error(ERROR_TRY_AGAIN.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
    @PatchMapping("/disable/{id}")
    @Operation(summary = "Disable product by id")
    public ResponseEntity<Object> disableProduct(@PathVariable("id") String id){
        productService.disableProduct(id);
        return ResponseEntity
                .ok(new SuccessResponse(HttpStatus.OK.value(),"Disable product successfully", null));

    }
    @PatchMapping("/enable/{id}")
    @Operation(summary = "Enable product by id")
    public ResponseEntity<Object> enableProduct(@PathVariable("id") String id){
        productService.enableProduct(id);
        return ResponseEntity
                .ok(new SuccessResponse(HttpStatus.OK.value(),"Disable product successfully", null));

    }
    private List<String> SortType(){
        List<String> list = new ArrayList<>();
        list.add("id");
        list.add("product:price_up");
        list.add("product:price_down");
        list.add("product:sell_amount");
        list.add("create_at");
        return list;
    }
}
