package com.abc.senki.controller.client;

import com.abc.senki.common.SortingEnum;
import com.abc.senki.model.entity.ProductEntity;
import com.abc.senki.model.payload.response.ErrorResponse;
import com.abc.senki.model.payload.response.SuccessResponse;
import com.abc.senki.service.CategoryService;
import com.abc.senki.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Join;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.abc.senki.common.SortingEnum.*;
import static com.abc.senki.common.ErrorDefinition.*;
import static com.abc.senki.service.implement.ProductServiceImpl.*;

@RestController
@RequestMapping("/api/product")
public class ProductController  {
    private static final String PRICE_ASC = "product:price_up";
    private static final String PRICE_DESC = "product:price_down";
    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;
    @GetMapping("")
    @Operation(summary = "Get all product")
    public ResponseEntity<Object> getAllProduct(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "20") int size,
                                                @RequestParam(defaultValue = "product_id") SortingEnum sort,
                                                @RequestParam(defaultValue = "0") Double minPrice,
                                                @RequestParam(defaultValue = "10000000") Double maxPrice) {
        System.out.println("sort: "+sort.getSort());

        try{
            if(!SortType().contains(sort.getSort())){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ErrorResponse.error(SORTING_TYPE_NOT_FOUND.getMessage(), HttpStatus.CONFLICT.value()));
            }
            Pageable paging = createPageRequest(page, size, sort.getSort());
            List<ProductEntity> list=productService.findPaginated(paging,minPrice,maxPrice);
            if(list.isEmpty()){
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(ErrorResponse.error(LIST_PRODUCT_EMPTY.getMessage(), HttpStatus.NO_CONTENT.value()));
            }
            else{
                Map<String, Object> data=new HashMap<>();
                data.put("list",list);
                return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(),"Get all product successfully",data));
            }

        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest()
                    .body(ErrorResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
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
    private Boolean isGreaterThanZero(int number){
        return number>0;
    }

    @GetMapping("/{id}/category")
    @Operation(summary = "Get product by category id")
    public ResponseEntity<Object> getProductByParent(@PathVariable String id,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(defaultValue = "product_id") SortingEnum sort,
                                                     @RequestParam(defaultValue = "0") Double min_price,
                                                     @RequestParam(defaultValue = "10000000") Double max_price
                                                     ) {
        try{
            if(!SortType().contains(sort.getSort())){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ErrorResponse.error(SORTING_TYPE_NOT_FOUND.getMessage(), HttpStatus.CONFLICT.value()));
            }
            Pageable paging = createPageRequest(page, size, sort.getSort());
            List<ProductEntity> list=productService.findAllByParent(id,paging,min_price,max_price);
            if(list.isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ErrorResponse.error(LIST_PRODUCT_EMPTY.getMessage(), HttpStatus.BAD_REQUEST.value()));
            }
            else{
                Map<String, Object> data=new HashMap<>();
                data.put("list",list);
                return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(),"Get product by parent category successfully",data));
            }
        }
        catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(ErrorResponse.error(ERROR_TRY_AGAIN.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
    @GetMapping("{id}")
    @Operation(summary = "Get product by id")
    public ResponseEntity<Object> getProductById(@PathVariable String id) {
        try{
            ProductEntity product=productService.findById(id);
            if(product==null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ErrorResponse.error(PRODUCT_NOT_FOUND.getMessage(), HttpStatus.BAD_REQUEST.value()));
            }
            else{
                Map<String, Object> data=new HashMap<>();
                data.put("product",product);
                return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(),"Get product by id successfully",data));
            }
        }
        catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(ErrorResponse.error(ERROR_TRY_AGAIN.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
    @GetMapping("search/{key}")
    @Operation(summary = "Search product by name, description")
    public ResponseEntity<Object> searchProductByName(@PathVariable String key,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "6") int size,
                                                      @RequestParam(defaultValue = "product_id") SortingEnum sort,
                                                      @RequestParam(defaultValue = "0") Double min_price,
                                                      @RequestParam(defaultValue = "10000000") Double max_price) {
        try{
            if(!SortType().contains(sort.getSort())){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ErrorResponse.error(SORTING_TYPE_NOT_FOUND.getMessage(), HttpStatus.CONFLICT.value()));
            }
            Pageable pageable=createPageRequest(page,size,sort.getSort());
            List<ProductEntity> list=productService.listAll(key,pageable,min_price,max_price);
            if(list.isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ErrorResponse.error(LIST_PRODUCT_EMPTY.getMessage(), HttpStatus.BAD_REQUEST.value()));
            }
            else{
                Map<String, Object> data=new HashMap<>();
                data.put("list",list);
                return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(),"Search product by name successfully",data));
            }
        }
        catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(ErrorResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }


    public Pageable createPageRequest(int pageNo, int pageSize, String sort) {
        return switch (sort) {
            case PRICE_ASC -> PageRequest.of(pageNo, pageSize, Sort.by("price").ascending());
            case PRICE_DESC -> PageRequest.of(pageNo, pageSize, Sort.by("price").descending());
            default -> PageRequest.of(pageNo, pageSize, Sort.by(sort).descending());
        };
    }


}
