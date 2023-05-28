package com.abc.senki.controller.shipper;

import com.abc.senki.handler.AuthenticationHandler;
import com.abc.senki.model.entity.OrderEntity;
import com.abc.senki.model.entity.UserEntity;
import com.abc.senki.model.payload.request.ShipperRequest.ShipperOrderMessage;
import com.abc.senki.model.payload.response.ErrorResponse;
import com.abc.senki.model.payload.response.SuccessResponse;
import com.abc.senki.service.OrderService;
import com.abc.senki.util.DataUtil;
import com.abc.senki.util.PageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/shipper/order")
@SecurityRequirement(name = "bearerAuth")
public class ShipperOrderController {
    @Autowired
    OrderService orderService;

    @Autowired
    AuthenticationHandler authenticationHandler;

    @GetMapping("")
    @Operation(summary = "Get all orders have same province of shipper")
    public ResponseEntity<Object> findAllOrderByShipper(HttpServletRequest request,
                                                        @RequestParam(required = false,defaultValue = "0") int pageNo,
                                                        @RequestParam(required = false,defaultValue = "6") int pageSize,
                                                        @RequestParam(required = false,defaultValue = "created_at") String sort){
        UserEntity shipper=authenticationHandler.userAuthenticate(request);
        if(shipper==null){
            return ResponseEntity.badRequest().body(new ErrorResponse("Cant get shipper info", HttpStatus.BAD_REQUEST.value()));
        }
        Pageable pageable=PageUtil.createPageRequestOrder(pageNo,pageSize,sort);
        String province=shipper.getAddress().getProvince().getId();
        //Get all order near shipper with processing status
        List<OrderEntity> orderList=orderService.findAllByProvinceAndValid(pageable,province,"processing");
        return ResponseEntity.ok(new SuccessResponse("Get data success",
                DataUtil.getData("list",orderList)));
    }
    @PatchMapping("/{id}")
    @Operation(summary = "Pick order for shipment")
    public ResponseEntity<Object> pickOrder(HttpServletRequest request,
                                            @PathVariable("id") UUID orderId){
        UserEntity shipper=authenticationHandler.userAuthenticate(request);
        if(shipper==null){
            return ResponseEntity.badRequest().body(new ErrorResponse("Cant get shipper info", HttpStatus.BAD_REQUEST.value()));
        }
        try{
            OrderEntity order=orderService.getOrderById(orderId);
            order.setShipper(shipper);
            orderService.saveOrder(order);
        }
        catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
        return ResponseEntity.ok(new SuccessResponse("Pick order success", null));
    }
    @PutMapping("/{id}")
    @Operation(summary = "Update order status/return order/cancel order")
    public ResponseEntity<Object> updateOrder(
                                              @PathVariable("id") UUID orderId,
                                              @Valid @RequestBody ShipperOrderMessage orderMessage){

        try{
            OrderEntity order=orderService.getOrderById(orderId);
            order.setStatus(orderMessage.getStatus());
            order.setNote(orderMessage.getNote());
            orderService.saveOrder(order);
        }
        catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
        return ResponseEntity.ok(new SuccessResponse("Pick order success", null));

    }
}
