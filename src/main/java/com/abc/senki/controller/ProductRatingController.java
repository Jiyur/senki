package com.abc.senki.controller;

import com.abc.senki.handler.AuthenticationHandler;
import com.abc.senki.handler.MethodArgumentNotValidException;
import com.abc.senki.model.entity.ProductEntity;
import com.abc.senki.model.entity.ProductRatingEntity;
import com.abc.senki.model.entity.UserEntity;
import com.abc.senki.model.payload.request.ProductRatingRequest.AddNewRatingRequest;
import com.abc.senki.model.payload.response.ErrorResponse;
import com.abc.senki.model.payload.response.SuccessResponse;
import com.abc.senki.service.ProductRatingService;
import com.abc.senki.service.ProductService;
import com.abc.senki.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.abc.senki.common.ErrorDefinition.*;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@RequestMapping("/api/rating")
public class ProductRatingController {
    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @Autowired
    AuthenticationHandler authenticationHandler;

    @Autowired
    ProductRatingService productRatingService;

    @Autowired
    ModelMapper mapper;
    @PostMapping("/add/{productId}")
    @Operation(summary = "Add rating for product")
    public ResponseEntity<Object> addRating(@PathVariable String productId,
                                            HttpServletRequest request,
                                            @RequestBody @Valid AddNewRatingRequest rating) {
        try{
            UserEntity user= authenticationHandler.userAuthenticate(request);
            if(user==null){
                return ResponseEntity.badRequest()
                        .body(ErrorResponse.error(USER_NOT_FOUND.getMessage(),HttpStatus.BAD_REQUEST.value()));
            }
            ProductEntity product=productService.findById(productId);
            if(product==null){
                return ResponseEntity.badRequest()
                        .body(ErrorResponse.error(PRODUCT_NOT_FOUND.getMessage(),HttpStatus.BAD_REQUEST.value()));
            }
            ProductRatingEntity ratingEntity=mapper.map(rating,ProductRatingEntity.class);
            ratingEntity.setCreatedAt(LocalDateTime.now());
            ratingEntity.setUser(user);
            ratingEntity.setProduct(product);
            productRatingService.saveRating(ratingEntity);
            Map<String,Object> data=new HashMap<>();
            data.put("rating",rating);
            return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "Save rating successful",data));
        }
        catch (Exception e ){
            return ResponseEntity.badRequest()
                    .body(ErrorResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }



    }
    @GetMapping("all/{productId}")
    @Operation(summary = "Get all rating for product")
    public ResponseEntity<Object> getAllRating(@PathVariable String productId){
        try{
            if(productId==null){
                return ResponseEntity.badRequest()
                        .body(ErrorResponse.error(PRODUCT_NOT_FOUND.getMessage(),HttpStatus.BAD_REQUEST.value()));
            }
            Map<String,Object> data=new HashMap<>();
            data.put("rating",productRatingService.getRatingByProductId(productId));
            return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "Get all rating successful",data));
        }
        catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(ErrorResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
    @GetMapping("all")
    @Operation(summary = "Get all rating for user")
    public ResponseEntity<Object> getAllRatingByUser(HttpServletRequest request){
        try{
            UserEntity user= authenticationHandler.userAuthenticate(request);
            if(user==null){
                return ResponseEntity.badRequest()
                        .body(ErrorResponse.error(USER_NOT_FOUND.getMessage(),HttpStatus.BAD_REQUEST.value()));
            }
            Map<String,Object> data=new HashMap<>();
            data.put("rating",productRatingService.getRatingByUserId(user.getId()));
            return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "Get all rating successful",data));
        }
        catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(ErrorResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }


}
