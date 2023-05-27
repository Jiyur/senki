package com.abc.senki.controller.client;

import com.abc.senki.model.entity.ProductEntity;
import com.abc.senki.model.payload.response.ErrorResponse;
import com.abc.senki.model.payload.response.SuccessResponse;
import com.abc.senki.service.ImageStorageService;
import com.abc.senki.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.abc.senki.common.ErrorDefinition.PRODUCT_NOT_FOUND;

@RestController
@RequestMapping("/api/image")
public class ImageController {
    @Autowired
    ProductService productService;
    @Autowired
    ImageStorageService imageStorageService;
    @CrossOrigin
    @PostMapping(value = "/upload/{id}",consumes = {"multipart/form-data"},produces = {"application/json"})
    @Operation(summary = "update image list for product")
    public ResponseEntity<Object> uploadImgInProduct(@RequestPart("files") MultipartFile[] files, @PathVariable String id,
                                                     HttpServletRequest request){
       try{
           ProductEntity product = productService.findById(id);
           System.out.println("TEST: "+request);
           if (product == null)
               return ResponseEntity
                       .badRequest().body(ErrorResponse.error(PRODUCT_NOT_FOUND.getMessage(), HttpStatus.OK.value()));
           List<String> urls = new ArrayList<>();
           int index = 0;
           for (MultipartFile file : files){
               if(!imageStorageService.isImageFile(file)){

                   return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                           .body(ErrorResponse.error("File is not image", HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()));
               }
           }
           imageStorageService.destroyProductImg(UUID.fromString(id));
           productService.deleteListImgProduct(product);
//
           for (MultipartFile file : files){
               index +=1;
               String url = imageStorageService.saveProductImg(file, id+"/"+"img"+index);
               urls.add(url);
               if(url.equals(""))
                   return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                           .body(ErrorResponse.error("Error when upload image", HttpStatus.BAD_REQUEST.value()));
           }
           product.setImageList(null);
           productService.saveListImageProduct(urls,product);
           HashMap<String,Object> data=new HashMap<>();

           return ResponseEntity
                   .ok(new SuccessResponse(HttpStatus.OK.value(),"Upload image successfully", null));
       }
       catch (Exception e){
           System.out.println(e.getMessage());
           return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                   .body(ErrorResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
       }

    }
}
