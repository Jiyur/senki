package com.abc.senki.controller;

import com.abc.senki.handler.AuthenticationHandler;
import com.abc.senki.model.entity.*;
import com.abc.senki.model.payload.request.OrderRequest.AddOrderRequest;
import com.abc.senki.model.payload.response.ErrorResponse;
import com.abc.senki.model.payload.response.SuccessResponse;
import com.abc.senki.service.OrderService;
import com.abc.senki.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/order")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {
    @Autowired
    AuthenticationHandler authenticationHandler;

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;
    @PostMapping("/add")
    @Operation(summary = "Add new order")
    public ResponseEntity<Object> addOrder(HttpServletRequest request, @RequestBody AddOrderRequest address
                                           ) {
        try{
            UserEntity user=authenticationHandler.userAuthenticate(request);
            if(user==null){
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("User not found", HttpStatus.BAD_REQUEST.value()));
            }
            CartEntity cart=user.getCart();
            if(cart==null){
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Cart not found", HttpStatus.BAD_REQUEST.value()));
            }
            if(cart.getCartItems().size()==0){
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Cart is empty", HttpStatus.BAD_REQUEST.value()));
            }
            OrderEntity order=new OrderEntity(user);
            String addressId= address.getAddressId();
            if(user.getAddress()
                    .stream()
                    .noneMatch(x->x.getId().equals(addressId))){
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Address not found", HttpStatus.BAD_REQUEST.value()));
            }
            order.setAddress(user.getAddress()
                    .stream()
                    .filter(x->x.getId().equals(addressId)).findFirst().orElse(null));
            //Save order
            orderService.saveOrder(order,cart);
            return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(),"Order successfully",null));
        }
        catch (Exception e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

}
