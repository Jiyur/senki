package com.abc.senki.controller;

import com.abc.senki.mapping.ProductMapping;
import com.abc.senki.model.entity.CategoryEntity;
import com.abc.senki.model.entity.ProductEntity;
import com.abc.senki.model.payload.request.ProductRequest.AddNewProductRequest;
import com.abc.senki.model.payload.response.ErrorResponse;
import com.abc.senki.model.payload.response.SuccessResponse;
import com.abc.senki.service.CategoryService;
import com.abc.senki.service.ImageStorageService;
import com.abc.senki.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static com.abc.senki.common.ErrorDefinition.*;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/admin/product/")
@RequiredArgsConstructor
public class AdminProductController {

    private final ModelMapper mapper;

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ImageStorageService imageStorageService;

    @PostMapping("insert")
    @Operation(summary = "Insert product")
    public ResponseEntity<Object> insertProduct(@RequestBody AddNewProductRequest request){
        CategoryEntity categoryEntity = categoryService.findById(request.getCate_id());
        ProductEntity product= ProductMapping.toEntity(request,categoryEntity);


        try{
            productService.saveProduct(product);
            return ResponseEntity
                    .ok(new SuccessResponse(HttpStatus.OK.value(),"Insert product successfully", null));
        }
        catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(ErrorResponse.error(ERROR_TRY_AGAIN.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
    @DeleteMapping("delete/{id}")
    @Operation(summary = "Delete product by id")
    public  ResponseEntity<Object> deleteProduct(@PathVariable("id") String id){
        try{
            productService.deleteProductById(id);
            return ResponseEntity
                    .ok(new SuccessResponse(HttpStatus.OK.value(),"Delete product successfully", null));
        }
        catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(ErrorResponse.error(ERROR_TRY_AGAIN.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
    @PutMapping("update/{id}")
    @Operation(summary = "Update product by id")
    public ResponseEntity<Object> updateProduct(@PathVariable("id") String id,
                                                @RequestBody AddNewProductRequest request){
        try{
            ProductEntity product=productService.findById(id);
            product.setInfo(request.getName(), request.getDescription(), request.getPrice(), request.getStock());
            productService.saveProduct(product);
            return ResponseEntity
                    .ok(new SuccessResponse(HttpStatus.OK.value(),"Update product successfully", null));
        }
        catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(ErrorResponse.error(ERROR_TRY_AGAIN.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
    @PostMapping(value = "/upload/{id}",consumes = {"multipart/form-data"})
    @Operation(summary = "update image list for product")
    public ResponseEntity<Object> uploadImgInProduct(@RequestPart(required = true) MultipartFile[] multipleFiles, @PathVariable String id){
        ProductEntity product = productService.findById(id);
        if (product == null)
            return ResponseEntity
                    .badRequest().body(ErrorResponse.error(PRODUCT_NOT_FOUND.getMessage(), HttpStatus.OK.value()));
        List<String> urls = new ArrayList<>();
        int index = 0;
        for (MultipartFile file : multipleFiles){
            if(!imageStorageService.isImageFile(file)){

                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                        .body(ErrorResponse.error("File is not image", HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()));
            }
        }
        imageStorageService.destroyProductImg(UUID.fromString(id));
        productService.deleteListImgProduct(product);
        for (MultipartFile file : multipleFiles){
            index +=1;
            String url = imageStorageService.saveProductImg(file, id+"/"+"img"+index);
            urls.add(url);
            if(url.equals(""))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                        .body(ErrorResponse.error("Error when upload image", HttpStatus.BAD_REQUEST.value()));
        }
        productService.saveListImageProduct(urls,product);
        return ResponseEntity
                .ok(new SuccessResponse(HttpStatus.OK.value(),"Upload image successfully", null));

    }

}
