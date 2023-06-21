package com.abc.senki.controller.global;

import com.abc.senki.handler.AuthenticationHandler;
import com.abc.senki.model.entity.RoleEntity;
import com.abc.senki.model.entity.UserEntity;
import com.abc.senki.model.payload.response.ErrorResponse;
import com.abc.senki.model.payload.response.SuccessResponse;
import com.abc.senki.repositories.RoleRepository;
import com.abc.senki.security.dto.AppUserDetail;
import com.abc.senki.security.jwt.JwtUtils;
import com.abc.senki.service.PaypalService;
import com.abc.senki.service.UserService;
import com.abc.senki.util.DataUtil;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@RestController
@RequestMapping("/api/license")
@SecurityRequirement(name = "bearerAuth")
@Slf4j
public class SellerLiscenseController {
    private static final Double TOTAL=149000.0;
    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationHandler authenticationHandler;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PaypalService paypalService;

    @Autowired
    private JwtUtils jwtUtils;

    private final static String REDIRECT_HOST="http://localhost:3000/paypal/success/";

    @GetMapping("")
    @Operation(summary = "Create seller license")
    public ResponseEntity<Object> createSellerLicense(HttpServletRequest request) throws URISyntaxException {
        UserEntity userEntity=authenticationHandler.userAuthenticate(request);
        //Check if user is seller or not
        if(isPresentRole(userEntity))
        {
            String userId=String.valueOf(userEntity.getId());

            String link=paypalService.paypalPaymentSellLicense(userId,request,TOTAL);
            if(link==null){
                return ResponseEntity.badRequest().body(new ErrorResponse("Payment error", HttpStatus.BAD_REQUEST.value()));
            }

            return  ResponseEntity.ok(new SuccessResponse("Place order success",
                    DataUtil.getData("link",link)
            ));
        }
        else{
            return ResponseEntity.badRequest().body(new ErrorResponse("You are not a seller/use", HttpStatus.BAD_REQUEST.value()));
        }
    }
    @GetMapping("/pay/success/{id}")
    @Operation(summary = "Paypal success")
    public ResponseEntity<Object> liscenseSuccess(@PathVariable("id") String userId,
                                                  @RequestParam(name = "paymentId") String paymentId,
                                                  @RequestParam(name = "PayerID") String payerId,
                                                  HttpServletResponse response
                                                  ) throws PayPalRESTException, IOException {
        UserEntity userEntity=userService.findById(UUID.fromString(userId));
        if(userEntity==null){
            return ResponseEntity.badRequest().body(new ErrorResponse("User not found", HttpStatus.BAD_REQUEST.value()));
        }
        //Check if user is seller or not
        Payment payment=paypalService.executePayment(paymentId,payerId);
        if(payment.getState().equals("approved")){
            Optional<RoleEntity> role=roleRepository.findByName("SELLER");
            if(role.isPresent()){
                if(userEntity.getRoles()==null){
                    Set<RoleEntity> roleSet=new HashSet<>();
                    roleSet.add(role.get());
                    userEntity.setRoles(roleSet);
                }
                else{
                    userEntity.getRoles().add(role.get());
                }
            }
            if(userEntity.getSellExpireDate()!=null){
                userEntity.setSellExpireDate(userEntity.getSellExpireDate().plusMonths(1));
            }
            else{
                userEntity.setSellExpireDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).plusMonths(1));
            }
            userService.saveInfo(userEntity);
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userEntity.getId().toString(),userEntity.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            AppUserDetail userDetail = (AppUserDetail) authentication.getPrincipal();
            //Set token
            String accessToken = "accessToken="+generateActiveToken(userDetail);
            String refreshToken = "refreshToken="+generateRefreshToken(userDetail);
            response.sendRedirect(REDIRECT_HOST+"?"+accessToken+"&"+refreshToken);
        }
        return ResponseEntity.badRequest().body(new ErrorResponse("Payment error", HttpStatus.BAD_REQUEST.value()));

    }
    public boolean isPresentRole(UserEntity userEntity){
        boolean flag=false;
        List<RoleEntity> roles=userEntity.getRoles().stream().toList();
        for (RoleEntity item:roles
        ) {
            if (Objects.equals(item.getName(), "USER")||Objects.equals(item.getName(), "SELLER")) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public String generateActiveToken(AppUserDetail userDetail) {
        return jwtUtils.generateJwtToken(userDetail);
    }

    public String generateRefreshToken(AppUserDetail userDetail) {
        return jwtUtils.generateRefreshJwtToken(userDetail);
    }

}
