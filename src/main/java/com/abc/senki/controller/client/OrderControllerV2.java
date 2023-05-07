package com.abc.senki.controller.client;

import com.abc.senki.handler.AuthenticationHandler;
import com.abc.senki.handler.BadRequestException;
import com.abc.senki.model.entity.OrderEntity;
import com.abc.senki.model.entity.UserEntity;
import com.abc.senki.model.payload.request.OrderRequest.CartItemList;
import com.abc.senki.model.payload.request.OrderRequest.CartItemRequest;
import com.abc.senki.model.payload.response.ErrorResponse;
import com.abc.senki.model.payload.response.SuccessResponse;
import com.abc.senki.service.OrderService;
import com.abc.senki.service.PaypalService;
import com.abc.senki.util.DataUtil;
import com.abc.senki.util.OrderUtil;
import com.paypal.api.payments.Order;
import com.paypal.api.payments.Payment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static com.abc.senki.common.OrderStatus.PENDING;
import static com.abc.senki.common.OrderStatus.PROCESSING;

@RestController
@Slf4j
@RequestMapping("api/v2/order")
@SecurityRequirement(name = "bearerAuth")
public class OrderControllerV2 {
    @Autowired
    private AuthenticationHandler authenticationHandler;

    @Autowired
    private PaypalService paypalService;

    @Autowired
    private OrderService orderService;


    @PostMapping("/paypal")
    @Operation(summary = "order with paypal")
    public ResponseEntity<Object> placePaypalOrder(HttpServletRequest request, @RequestBody List<CartItemRequest> cartList){
        UserEntity user=authenticationHandler.userAuthenticate(request);
        //Init order & process
        String payId=user.getId().toString()+new Date().getTime();
        List<OrderEntity> orderList=OrderUtil.handleCart(cartList,user,payId);
        Double total=0.0;
        for (OrderEntity order:orderList
             ) {
            total+=order.getTotal();
            order.setMethod("PAYPAL");
            order.setStatus(PENDING.getMessage());
        }
        //Generate paypal link
        String link=paypalService.paypalPaymentV2(payId,request,total);
        log.info(link);
        if(link==null){
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Paypal payment error", HttpStatus.BAD_REQUEST.value()));
        }
        orderService.saveOrderList(orderList);

        return  ResponseEntity.ok(new SuccessResponse("Place order success",
                DataUtil.getData("link",link)
                ));

    }
    @GetMapping("/pay/success/{id}")
    @Operation(summary = "Paypal payment success")
    public ResponseEntity<Object> successPay(@PathVariable("id") String id,
                                             @RequestParam("paymentId") String paymentId,
                                             @RequestParam("redirectURI") String uri,
                                             @RequestParam("PayerID") String payerId
                                            ){
        //Execute payment
        try{
            Payment payment=paypalService.executePayment(paymentId,payerId);
            System.out.println(uri);
            if(payment.getState().equals("approved")){
                Map<String,Object> data=new HashMap<>();
                orderService.updateAllOrderStatus(UUID.fromString(id),PROCESSING.getMessage());
                //Process order if payment success
                data.put("payId",id);
                return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(),"Payment success",data));
            }
            throw new BadRequestException("Payment failed");

        }
        catch (Exception e){
            throw new BadRequestException(e.getMessage());
        }
    }



}
