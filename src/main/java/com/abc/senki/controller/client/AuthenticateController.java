package com.abc.senki.controller.client;

import com.abc.senki.handler.AuthenticationHandler;
import com.abc.senki.handler.HttpMessageNotReadableException;
import com.abc.senki.handler.MethodArgumentNotValidException;
import com.abc.senki.handler.RecordNotFoundException;
import com.abc.senki.model.entity.UserEntity;
import com.abc.senki.model.payload.request.AuthenticationRequest.RefreshTokenRequest;
import com.abc.senki.model.payload.request.AuthenticationRequest.ResetPasswordRequest;
import com.abc.senki.model.payload.request.UserRequest.AddNewUserRequest;
import com.abc.senki.model.payload.request.UserRequest.ForgetPasswordRequest;
import com.abc.senki.model.payload.request.UserRequest.UserLoginRequest;
import com.abc.senki.model.payload.response.ErrorResponse;
import com.abc.senki.model.payload.response.SuccessResponse;
import com.abc.senki.security.dto.AppUserDetail;
import com.abc.senki.security.jwt.JwtUtils;
import com.abc.senki.service.EmailService;
import com.abc.senki.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.UUID;

import static com.abc.senki.common.ErrorDefinition.ERROR_TRY_AGAIN;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/auth/")
public class AuthenticateController {
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    AuthenticationHandler authenticationHandler;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private EmailService emailService;
    @PostMapping("refreshToken")
    @Operation(summary = "Refresh token")
    public ResponseEntity<Object> refreshToken(@RequestBody RefreshTokenRequest refreshToken
            , HttpServletResponse response, HttpServletRequest request) {
        try {
           String accessToken=request.getHeader("Authorization").substring("Bearer ".length());
           if(!jwtUtils.validateExpiredToken(accessToken)){
               return ResponseEntity.badRequest()
                       .body(ErrorResponse.error("Token is not expired", HttpStatus.BAD_REQUEST.value()));
           }
           if(jwtUtils.validateExpiredToken(refreshToken.getRefreshToken())){
               return ResponseEntity.badRequest()
                       .body(ErrorResponse.error("Refresh token is expired", HttpStatus.BAD_REQUEST.value()));
           }
           if(refreshToken==null){
               return ResponseEntity.badRequest()
                       .body(ErrorResponse.error("Refresh token is missing", HttpStatus.BAD_REQUEST.value()));
           }
            AppUserDetail userDetails = authenticationHandler
                    .refreshAuthenticate(refreshToken.getRefreshToken());
            accessToken=generateActiveToken(userDetails);
            Cookie cookie = new Cookie(ACCESS_TOKEN, accessToken);

            response.setHeader("Set-Cookie", "test=value; Path=/");
            response.addCookie(cookie);
            HashMap<String,Object> data=new HashMap<>();
            data.put("accessToken",accessToken);
            data.put("refreshToken",refreshToken.getRefreshToken());

            return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "Refresh token successfully",data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.error(e.getMessage(), HttpStatus.UNAUTHORIZED.value()));

        }
    }
    @PostMapping("register")
    @Operation(summary = "Register new user")
    public ResponseEntity<Object> register(@RequestBody @Valid AddNewUserRequest request) {
        request.setPassword(encoder.encode(request.getPassword()));

        UserEntity userEntity = mapper.map(request, UserEntity.class);
        userEntity.setCreateAt(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));

        if (Boolean.TRUE.equals(userService.existsByEmail(userEntity.getEmail()))) {
            return ResponseEntity.badRequest()
                    .body(ErrorResponse.error("This email already exists", HttpStatus.BAD_REQUEST.value()));
        }
        try {
            userService.saveUser(userEntity, "USER");
            HashMap<String, Object> data = new HashMap<>();
            data.put("user", userEntity.getEmail());
            return ResponseEntity
                    .ok(new SuccessResponse(HttpStatus.OK.value(), "Register successfully", data));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ErrorResponse.error("Your submition failed, please try again later", HttpStatus.BAD_REQUEST.value()));
        }
    }

    @PostMapping("login")
    @Operation(summary = "Login user")
    public ResponseEntity<Object> login(@RequestBody @Valid UserLoginRequest user, HttpServletResponse resp) {
        if (Boolean.FALSE.equals(userService.existsByEmail(user.getEmail()))) {
            return ResponseEntity.badRequest().body(ErrorResponse.error("This email not valid", HttpStatus.BAD_REQUEST.value()));
        }
        UserEntity loginUser = userService.findByEmail(user.getEmail());
        if (!encoder.matches(user.getPassword(), loginUser.getPassword())) {
            return ResponseEntity.badRequest().body(ErrorResponse.error("Wrong password", HttpStatus.BAD_REQUEST.value()));
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUser.getId().toString(), user.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        AppUserDetail userDetail = (AppUserDetail) authentication.getPrincipal();

        String accessToken = generateActiveToken(userDetail);
        String refreshToken = generateRefreshToken(userDetail);
        HashMap<String, Object> data = new HashMap<>();
        data.put(ACCESS_TOKEN, accessToken);
        data.put("refreshToken", refreshToken);
        data.put("user", loginUser);

        resp.setHeader("Set-Cookie", "test=value; Path=/");
        resp.addCookie(new Cookie(ACCESS_TOKEN, accessToken));
        return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "Login success", data));

    }

    @GetMapping("/social")
    @Operation(summary = "Login with social account")
    public ResponseEntity<Object> socialToken(
            @RequestParam(defaultValue = "") String token,
            HttpServletResponse resp) {
        try {
            if (token == null || token.equals("")) {
                throw new BadCredentialsException("token is not valid");
            }
            String email = jwtUtils.getUserNameFromJwtToken(token);
            UserEntity user = userService.findByEmail(email);
            if (user == null) {
                throw new RecordNotFoundException("Not found, please register again");
            }
            AppUserDetail userDetails = AppUserDetail.build(user);
            String accessToken = generateActiveToken(userDetails);
            String refreshToken = generateRefreshToken(userDetails);

            HashMap<String, Object> data = new HashMap<>();
            data.put(ACCESS_TOKEN, accessToken);
            data.put(REFRESH_TOKEN, refreshToken);
            data.put("user", user);

            resp.setHeader("Set-Cookie", "test=value; Path=/");
            resp.addCookie(new Cookie(ACCESS_TOKEN, accessToken));
            resp.addCookie(new Cookie("refreshToken", refreshToken));

            return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "Login success", data));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ErrorResponse.error(ERROR_TRY_AGAIN.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
    @PostMapping("/resetPassword")
    @Operation(summary = "Reset password")
    public ResponseEntity<Object> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
       try{
           if(request.getToken()==null||request.getToken().equals("")){
               return ResponseEntity.badRequest()
                       .body(ErrorResponse.error("Token is missing", HttpStatus.BAD_REQUEST.value()));
           }
           if(request.getPassword()==null||request.getPassword().equals("")){
               return ResponseEntity.badRequest()
                       .body(ErrorResponse.error("Password is missing", HttpStatus.BAD_REQUEST.value()));
           }
           if(!request.getConfirmPassword().equals(request.getPassword())){
                return ResponseEntity.badRequest()
                          .body(ErrorResponse.error("Confirm password is not match", HttpStatus.BAD_REQUEST.value()));
           }
           if(request.getPassword().equals(request.getConfirmPassword())){
               String email=jwtUtils.getUserNameFromJwtToken(request.getToken());
               UserEntity user=userService.findByEmail(email);
               user.setPassword(encoder.encode(request.getPassword()));
               userService.saveInfo(user);
               return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "Reset password successfully",null));
           }
              return ResponseEntity.badRequest()
                     .body(ErrorResponse.error("Reset password failed", HttpStatus.BAD_REQUEST.value()));
           

       }
       catch (Exception e){
              return ResponseEntity.badRequest()
                     .body(ErrorResponse.error("Your submition failed, please try again later", HttpStatus.BAD_REQUEST.value()));

       }
    }

    @PostMapping("/forgetPassword")
    @Operation(summary = "Restore password by email")
    public ResponseEntity<Object> forgetPassword(@RequestBody @Valid ForgetPasswordRequest request, BindingResult errors,
                                                 HttpServletRequest req) throws MethodArgumentNotValidException {
        try {
//            if (errors.hasErrors()) {
//                throw new MethodArgumentNotValidException(errors);
//            }
//            if (request == null) {
//                throw new HttpMessageNotReadableException("Missing field");
//            }
            if (userService.findByEmail(request.getEmail()) == null) {
                throw new HttpMessageNotReadableException("Email is not Registered");
            }
            UserEntity user = userService.findByEmail(request.getEmail());
            emailService.sendGridEmail(req.getHeader("Origin"),user);
            HashMap<String, Object> data = new HashMap<>();
            data.put("email", user.getEmail());
            return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "Email sent successfully", data));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ErrorResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    public String generateActiveToken(AppUserDetail userDetail) {
        return jwtUtils.generateJwtToken(userDetail);
    }

    public String generateRefreshToken(AppUserDetail userDetail) {
        return jwtUtils.generateRefreshJwtToken(userDetail);
    }
}
