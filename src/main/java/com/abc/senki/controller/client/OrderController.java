package com.abc.senki.controller.client;

import com.abc.senki.handler.AuthenticationHandler;
import com.abc.senki.model.entity.*;
import com.abc.senki.model.payload.request.OrderRequest.CartItemList;
import com.abc.senki.model.payload.response.ErrorResponse;
import com.abc.senki.model.payload.response.SuccessResponse;
import com.abc.senki.service.OrderService;
import com.abc.senki.service.PaypalService;
import com.abc.senki.service.UserService;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.abc.senki.common.OrderStatus.*;

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
    @PostMapping("/paypal/{id}")
    @Operation(summary = "Get paypal payment link")
    public ResponseEntity<Object> paypalLinkGenerate(HttpServletRequest request,@PathVariable UUID id){
        try{
            UserEntity user=authenticationHandler.userAuthenticate(request);
            if(user==null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("Unauthorized",HttpStatus.UNAUTHORIZED.value()));
            }
            OrderEntity order=orderService.getOrderById(id);
            if(order.getUser().equals(user)&&order.getMethod().toString().equals("PAYPAL")){
                String link=paypalService.paypalPayment(order,request);
               if(link!=null){
                   HashMap<String,Object> data=new HashMap<>();
                   data.put("link",link);
                   return ResponseEntity
                           .ok(new SuccessResponse(HttpStatus.OK.value(),"Generate success",data));
               }

            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Cannot create payment",HttpStatus.BAD_REQUEST.value()));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(new ErrorResponse("Failed to generate link",HttpStatus.BAD_REQUEST.value()));
        }
    }
    @PostMapping("paypal")
    @Operation(summary = "Add PAYPAL order")
    public ResponseEntity<Object> addPayPalOrder(HttpServletRequest request,@RequestBody List<CartItemList> cartList){
        try{
            UserEntity user=authenticationHandler.userAuthenticate(request);
            if(user==null){
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("User not found", HttpStatus.BAD_REQUEST.value()));
            }

            OrderEntity order=new OrderEntity(user);
            //Set order address
            OrderAddress address=new OrderAddress();
            address.setDistrict(user.getAddress().getDistrict());
            address.setProvince(user.getAddress().getProvince());
            address.setCommune(user.getAddress().getCommune());
            address.setInfo(
                   user.getAddress().getFullName(),
                     user.getAddress().getCompanyName(), user.getAddress().getPhoneNumber(),
                    user.getAddress().getProvince(),
                    user.getAddress().getDistrict(),
                    user.getAddress().getCommune(),
                    user.getAddress().getAddressDetail()
            );
            order.setAddress(address);

            if(order.getAddress()==null){
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Address not found", HttpStatus.BAD_REQUEST.value()));
            }
            //Calculate total
            cartProcess(order,cartList);
            //Process Payment
            String link=paypalService.paypalPayment(order,request);
            if(link==null){
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Paypal payment error", HttpStatus.BAD_REQUEST.value()));
            }
            //Save order
            order.setMethod("PAYPAL");
            order.setStatus(PENDING.getMessage());
            orderService.saveOrder(order);
            //Response
            HashMap<String,Object> data=new HashMap<>();
            data.put("link",link);
            return ResponseEntity
                    .ok(new SuccessResponse(HttpStatus.OK.value(),"PAYPAL Payment",data));
            
        }
        catch (Exception e){
            return ResponseEntity.status(400).body(new ErrorResponse(e.getMessage(),HttpStatus.BAD_REQUEST.value()));
        }
    }
    @GetMapping("{id}")
    @Operation(summary = "Get order by id")
    public ResponseEntity<Object> getOrderDetailById(@PathVariable UUID id,HttpServletRequest request){
        try{
            UserEntity user=authenticationHandler.userAuthenticate(request);
            if(user==null){
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("User not found", HttpStatus.BAD_REQUEST.value()));
            }
            OrderEntity order=orderService.getOrderById(id);
            if(order==null){
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Order not found", HttpStatus.BAD_REQUEST.value()));
            }
            if(!order.getUser().getId().equals(user.getId())){
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Order not found", HttpStatus.BAD_REQUEST.value()));
            }
            HashMap<String,Object> data=new HashMap<>();
            data.put("order",order);
            return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(),"Get order success",data));
        }
        catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Can't get the specificed order", HttpStatus.BAD_REQUEST.value()));
        }
    }

    @PostMapping("cod")
    @Operation(summary = "Add COD order")
    public ResponseEntity<Object> addCODOrder(HttpServletRequest request,
                                              @RequestBody List<CartItemList> cartList
    ) {
        try{
            UserEntity user=authenticationHandler.userAuthenticate(request);
            if(user==null){
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("User not found", HttpStatus.BAD_REQUEST.value()));
            }

            OrderEntity order=new OrderEntity(user);
            //Set order address
            OrderAddress address=new OrderAddress();
            address.setDistrict(user.getAddress().getDistrict());
            address.setProvince(user.getAddress().getProvince());
            address.setCommune(user.getAddress().getCommune());
            address.setInfo(
                    user.getAddress().getFullName(),
                    user.getAddress().getCompanyName(), user.getAddress().getPhoneNumber(),
                    user.getAddress().getProvince(),
                    user.getAddress().getDistrict(),
                    user.getAddress().getCommune(),
                    user.getAddress().getAddressDetail()
            );
            order.setAddress(address);
            if(order.getAddress()==null){
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Address not found", HttpStatus.BAD_REQUEST.value()));
            }
            //Calculate total
            cartProcess(order,cartList);
            //Save order
            order.setMethod("COD");
            order.setStatus(PROCESSING.getMessage());
            orderService.saveOrder(order);
            return ResponseEntity
                    .ok(new SuccessResponse(HttpStatus.OK.value(),"Order successfully",null));

        }
        catch (Exception e){
            return ResponseEntity.status(400).body(new ErrorResponse(e.getMessage(),HttpStatus.BAD_REQUEST.value()));
        }
    }

    @GetMapping("/pay/success/{id}")
    @Operation(summary = "Paypal payment success")
    public ResponseEntity<Object> successPay(@PathVariable String id,
                                             @RequestParam("paymentId") String paymentId,
                                             @RequestParam("PayerID") String payerId,
                                             HttpServletResponse response){
        //Execute payment
        try{
            Payment payment=paypalService.executePayment(paymentId,payerId);
            if(payment.getState().equals("approved")){
                Map<String,Object> data=new HashMap<>();
                orderService.updateOrderStatus(UUID.fromString(id),PROCESSING.getMessage());
                //Process order if payment success
                data.put("orderId",id);
                response.sendRedirect("http://localhost:3000/paypal/success?orderId="+id);
                return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(),"Payment success",data));
            }
        }
        catch (PayPalRESTException e){
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(),HttpStatus.BAD_REQUEST.value()));
        }
        catch (IOException e1){
            return ResponseEntity.badRequest().body(new ErrorResponse(e1.getMessage(),HttpStatus.BAD_REQUEST.value()));
        }
        return ResponseEntity.badRequest().body(new ErrorResponse("Payment failed",HttpStatus.BAD_REQUEST.value()));
    }
    @GetMapping("/pay/cancel/{id}")
    @Operation(summary = "Paypal payment cancel")
    public ResponseEntity<Object> cancelPay(@PathVariable String id){
        orderService.updateOrderStatus(UUID.fromString(id),CANCELLED.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse("Payment cancel",HttpStatus.BAD_REQUEST.value()));
    }

    //Process COD order
    public void cartProcess(OrderEntity order, List<CartItemList> listCart){
        List<OrderDetailEntity> orderDetailList = new ArrayList<>();
        for (CartItemList cartItem:listCart)
        {
            OrderDetailEntity orderDetail=new OrderDetailEntity();
            orderDetail.setInfo(order,
                    cartItem.getProductName(),
                    cartItem.getProductId(),
                    cartItem.getProductImage(),
                    cartItem.getQuantity(),
                    cartItem.getPrice());
            orderDetailList.add(orderDetail);
            order.setTotal(order.getTotal()+cartItem.getPrice()*cartItem.getQuantity());
        }
        //Set item and delete cart
        order.setOrderDetails(orderDetailList);
        order.setTotal(order.getTotal()+order.getShipFee());
    }



}
