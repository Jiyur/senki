package com.abc.senki.controller.shipper;

import com.abc.senki.handler.AuthenticationHandler;
import com.abc.senki.model.entity.OrderEntity;
import com.abc.senki.model.entity.UserEntity;
import com.abc.senki.model.payload.response.ErrorResponse;
import com.abc.senki.model.payload.response.SuccessResponse;
import com.abc.senki.service.OrderService;
import com.abc.senki.util.DataUtil;
import com.abc.senki.util.PageUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("api/shipper")
public class ShipperController {
    @Autowired
    OrderService orderService;

    @Autowired
    AuthenticationHandler authenticationHandler;

    @GetMapping("/order/list")
    @Operation(summary = "")
    public ResponseEntity<Object> findAllOrderByShipper(HttpServletRequest request,
                                                        @RequestParam(required = false,defaultValue = "0") int pageNo,
                                                        @RequestParam(required = false,defaultValue = "6") int pageSize,
                                                        @RequestParam(required = false,defaultValue = "creatAt") String sort){
        UserEntity shipper=authenticationHandler.userAuthenticate(request);
        if(shipper==null){
            return ResponseEntity.badRequest().body(new ErrorResponse("Cant get shipper info", HttpStatus.BAD_REQUEST.value()));
        }
        Pageable pageable=PageUtil.createPageRequestOrder(pageNo,pageSize,sort);
        List<OrderEntity> orderList=orderService.findAllByProvinceAndValid(pageable,shipper.getAddress().getProvince().getId());
        return ResponseEntity.ok(new SuccessResponse("Get data success",
                DataUtil.getData("list",orderList)));
    }
}
