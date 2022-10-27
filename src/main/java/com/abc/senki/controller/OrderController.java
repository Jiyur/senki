package com.abc.senki.controller;

import com.abc.senki.handler.AuthenticationHandler;
import com.abc.senki.model.entity.*;
import com.abc.senki.model.payload.request.OrderRequest.AddOrderRequest;
import com.abc.senki.model.payload.response.ErrorResponse;
import com.abc.senki.model.payload.response.SuccessResponse;
import com.abc.senki.service.OrderService;
import com.abc.senki.service.PaypalService;
import com.abc.senki.service.UserService;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
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
import java.util.*;

import com.abc.senki.common.OrderStatus.*;

import static com.abc.senki.common.OrderStatus.PROCESSING;

@RestController
@RequestMapping("/api/order")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {
    @Autowired
    AuthenticationHandler authenticationHandler;

    public static final String SUCCESS_URL = "/api/order/pay/success";
    public static final String CANCEL_URL = "/api/order/pay/cancel";


    @Autowired
    UserService userService;
    @Autowired
    PaypalService paypalService;
    @Autowired
    OrderService orderService;
    @PostMapping("/add")
    @Operation(summary = "Add new order")
    public ResponseEntity<Object> addOrder(HttpServletRequest request, @RequestBody AddOrderRequest orderRequest
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
            //Set order address
            order.setAddress(user.getAddress());

            if(order.getAddress()==null){
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Address not found", HttpStatus.BAD_REQUEST.value()));
            }
            //Calculate total
            orderCalculation(order,cart);
            //Save order
            String payMethod=orderRequest.getPaymentMethod();
            switch (payMethod) {
                case "COD" -> {
                    order.setMethod("COD");
                    order.setStatus(PROCESSING.getMesssage());
                }
                case "PAYPAL" -> {
                    String link=paypalService.paypalPayment(order,request);
                    if(link==null){
                        return ResponseEntity.badRequest()
                                .body(new ErrorResponse("Paypal payment error", HttpStatus.BAD_REQUEST.value()));
                    }
                    order.setMethod("PAYPAL");
                    order.setStatus(PROCESSING.getMesssage());
                }
                default -> {
                    return ResponseEntity.badRequest()
                            .body(new ErrorResponse("Payment method not found", HttpStatus.BAD_REQUEST.value()));
                }
            }

            orderService.saveOrder(order,cart);
            return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(),"Order successfully",null));
        }
        catch (Exception e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
    @GetMapping("/pay/success/{id}")
    @Operation(summary = "Paypal payment success")
    public ResponseEntity<Object> successPay(@PathVariable String id,
                                             @RequestParam("paymentId") String paymentId,
                                             @RequestParam("PayerID") String payerId){
        try{
            Payment payment=paypalService.executePayment(paymentId,payerId);
            System.out.println(payment.toJSON());
            if(payment.getState().equals("approved")){
                Map<String,Object> data=new HashMap<>();
                data.put("orderId",id);
                return ResponseEntity.ok(
                        new SuccessResponse(HttpStatus.OK.value(),"Payment success",data));
            }
        }
        catch (PayPalRESTException e){
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(),HttpStatus.BAD_REQUEST.value()));
        }
        return ResponseEntity.badRequest().body(new ErrorResponse("Payment failed",HttpStatus.BAD_REQUEST.value()));
    }
    @GetMapping("/pay/cancel/{id}")
    @Operation(summary = "Paypal payment cancel")
    public ResponseEntity<Object> cancelPay(@PathVariable String id){
        return ResponseEntity.badRequest().body(new ErrorResponse("Payment cancel",HttpStatus.BAD_REQUEST.value()));
    }
    public void orderCalculation(OrderEntity order, CartEntity cart){
        List<OrderDetailEntity> orderDetailList = new ArrayList<>();
        for (CartItemEntity cartItem:cart.getCartItems())
        {
            OrderDetailEntity orderDetail=new OrderDetailEntity();
            orderDetail.setInfo(order,
                    cartItem.getProduct(),
                    cartItem.getAttributeValue(),
                    cartItem.getQuantity(),
                    cartItem.getProduct().getPrice());
            orderDetailList.add(orderDetail);
            order.setTotal(order.getTotal()+cartItem.getProduct().getPrice()*cartItem.getQuantity());
        }
        //Set item and delete cart
        order.setOrderDetails(orderDetailList);

        order.setTotal(order.getTotal()+order.getShipFee());
    }



}
