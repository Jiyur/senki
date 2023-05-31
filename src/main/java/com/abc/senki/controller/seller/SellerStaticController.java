package com.abc.senki.controller.seller;

import com.abc.senki.handler.AuthenticationHandler;
import com.abc.senki.model.entity.UserEntity;
import com.abc.senki.service.StaticService;
import com.abc.senki.util.DataUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("api/seller/static")
@RestController
@SecurityRequirement(name = "bearerAuth")
public class SellerStaticController {
    @Autowired
    AuthenticationHandler authenticationHandler;
    @Autowired
    StaticService staticService;
    @GetMapping("{year}")
    @Operation(summary = "Get static by seller - by year - all month")
    public ResponseEntity<Object> getStaticBySeller(HttpServletRequest request,
                                                    @PathVariable("year") String year) {
        UserEntity seller=authenticationHandler.userAuthenticate(request);
        return ResponseEntity.ok(DataUtil.getData("data",staticService.getStaticByYear(seller,year)));
    }
}
