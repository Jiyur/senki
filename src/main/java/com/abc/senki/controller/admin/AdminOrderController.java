package com.abc.senki.controller.admin;


import com.abc.senki.handler.AuthenticationHandler;
import com.abc.senki.model.entity.OrderEntity;
import com.abc.senki.model.entity.UserEntity;
import com.abc.senki.model.payload.request.OrderRequest.OrderRequestStatus;
import com.abc.senki.model.payload.response.ErrorResponse;
import com.abc.senki.model.payload.response.SuccessResponse;
import com.abc.senki.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.abc.senki.common.ErrorDefinition.ACCESS_TOKEN_EXPIRED;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("api/admin/order")
public class AdminOrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    AuthenticationHandler authenticationHandler;
    @GetMapping("")
    @Operation(summary = "Get all order ")
    public ResponseEntity<Object> getAllOrder(HttpServletRequest request,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "20") int size){
        UserEntity user=authenticationHandler.userAuthenticate(request);
        try{
            if(user==null){
                throw new BadCredentialsException(ACCESS_TOKEN_EXPIRED.getMessage());
            }
            else{
                Pageable pageable= PageRequest.of(page,size, Sort.by("createdAt").descending());
                HashMap<String,Object> data=new HashMap<>();
                List<OrderEntity> list=orderService.getAllOrder(pageable);
                data.put("orders",list);
                return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(),"Get order successfully",data));
            }
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(ErrorResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
    @GetMapping("{id}")
    @Operation(summary = "Get order by id")
    public ResponseEntity<Object> getOrderDetailById(@PathVariable UUID id, HttpServletRequest request){
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
            HashMap<String,Object> data=new HashMap<>();
            data.put("order",order);
            return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(),"Get order success",data));
        }
        catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Can't get the specified order", HttpStatus.BAD_REQUEST.value()));
        }
    }
    @PatchMapping("{id}")
    @Operation(summary = "Update order status")
    public ResponseEntity<Object> updateOrderStatus(@PathVariable UUID id, HttpServletRequest request,
                                                    @RequestBody OrderRequestStatus status){
        try{
            UserEntity user=authenticationHandler.userAuthenticate(request);
            if(user==null){
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("User not found", HttpStatus.BAD_REQUEST.value()));
            }
            String orderStatus=status.getStatus();
            if(!orderStatus.isBlank()){
                orderService.updateOrderStatus(id,orderStatus);

            }
            return ResponseEntity.ok(
                    new SuccessResponse(HttpStatus.OK.value(),"Update status success",null)
            );

        }
        catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Failed to update order status", HttpStatus.BAD_REQUEST.value()));
        }
    }
}
