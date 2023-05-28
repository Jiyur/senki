package com.abc.senki.controller.seller;

import com.abc.senki.handler.AuthenticationHandler;
import com.abc.senki.model.entity.OrderEntity;
import com.abc.senki.model.entity.UserEntity;
import com.abc.senki.model.payload.response.ErrorResponse;
import com.abc.senki.model.payload.response.SuccessResponse;
import com.abc.senki.service.OrderService;
import com.abc.senki.util.DataUtil;
import com.abc.senki.util.PageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name="bearerAuth")
@RequestMapping("api/seller/order")
public class SellerOrderController {
    @Autowired
    private AuthenticationHandler authenticationHandler;

    @Autowired
    private OrderService orderService;

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
            data.put("data",order);
            return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(),"Get order success",data));
        }
        catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Can't get the specified order", HttpStatus.BAD_REQUEST.value()));
        }
    }
    @GetMapping("")
    @Operation(summary = "Get order by seller")
    public ResponseEntity<Object> listOrderBySeller(HttpServletRequest request,
                                                    @RequestParam(required = false,defaultValue = "0") int pageNo,
                                                    @RequestParam(required = false,defaultValue = "6") int pageSize,
                                                    @RequestParam(required = false,defaultValue = "createdAt") String sort){
        Pageable pageable= PageUtil.createPageRequestOrder(pageNo,pageSize,sort);
        UserEntity seller=authenticationHandler.userAuthenticate(request);
        List<OrderEntity> orderList=orderService.findAllBySellerId(seller.getId(),pageable);
        return ResponseEntity.ok(DataUtil
                .getData(
                        "data",orderList));

    }
}
