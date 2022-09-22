package com.abc.senki.controller;

import com.abc.senki.handler.AuthenticationHandler;
import com.abc.senki.model.entity.UserEntity;
import com.abc.senki.model.payload.request.UserRequest.ChangePasswordRequest;
import com.abc.senki.model.payload.request.UserRequest.ChangeUserInfoRequest;
import com.abc.senki.model.payload.request.UserRequest.ForgetPasswordRequest;
import com.abc.senki.model.payload.response.ErrorResponse;
import com.abc.senki.model.payload.response.SuccessResponse;
import com.abc.senki.service.ImageStorageService;
import com.abc.senki.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;


@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/user/")
public class UserController {
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserService userService;
    @Autowired
    AuthenticationHandler authenticationHandler;
    @Autowired
    ImageStorageService imageStorageService;


    @GetMapping("profile")
    @Operation(summary = "Get all user")
    public ResponseEntity<Object> profile(HttpServletRequest request){
        UserEntity userEntity= authenticationHandler.userAuthenticate(request);
        if(userEntity==null){
            throw new BadCredentialsException("access token is expired");
        }
        else{
            HashMap<String,Object> data=new HashMap<>();
            data.put("user",userEntity);
            return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(),"Get user profile successfully",data));
        }
    }
    @PutMapping("profile/changeInfo")
    @Operation(summary = "Change user info")
    public ResponseEntity<Object> changeInfo(HttpServletRequest request,@RequestBody @Valid ChangeUserInfoRequest user) {
        UserEntity userEntity = authenticationHandler.userAuthenticate(request);
        if(userEntity==null){
            throw new BadCredentialsException("access token is expired");
        }
        else{
            userEntity.setFullName(user.getFullName());
            userEntity.setGender(user.getGender());
            userEntity.setNickName(user.getNickName());
            userService.saveInfo(userEntity);
            HashMap<String,Object> data=new HashMap<>();
            data.put("user","");
            return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(),"Change profile successfully",data));
        }
    }
    @PutMapping("profile/changeEmail")
    @Operation(summary = "Change user email")
    public ResponseEntity<Object> changeEmail(HttpServletRequest request,@RequestBody @Valid ForgetPasswordRequest user) {
        UserEntity userEntity = authenticationHandler.userAuthenticate(request);
        if(userEntity==null){
            throw new BadCredentialsException("access token is expired");
        }
        else{
            if(Boolean.TRUE.equals(userService.existsByEmail(user.getEmail()))){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ErrorResponse.error("Email is already exist", HttpStatus.CONFLICT.value()));
            }
            else{
                userEntity.setEmail(user.getEmail());
                userService.saveInfo(userEntity);
                HashMap<String,Object> data=new HashMap<>();
                return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(),"Change email successfully",data));
            }
        }
    }
    @PutMapping("/profile/changePassword")
    @Operation(summary = "Change user password")
    public ResponseEntity<Object> changePassword(HttpServletRequest req, @RequestBody @Valid ChangePasswordRequest request) throws BadCredentialsException{
        UserEntity user=authenticationHandler.userAuthenticate(req);
        try{
            if(user==null){
                throw new BadCredentialsException("User not found");
            }
            else{
                if(!encoder.matches(request.getOldPassword(),user.getPassword())){
                    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                            .body(ErrorResponse.error("Old password is incorrect", HttpStatus.UNPROCESSABLE_ENTITY.value()));
                }
                if(!request.getNewPassword().equals(request.getConfirmPassword())){
                    return  ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                            .body(ErrorResponse.error("Confirm password is incorrect", HttpStatus.UNPROCESSABLE_ENTITY.value()));
                }
                user.setPassword(encoder.encode(request.getNewPassword()));
                userService.saveInfo(user);

                return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(),"Change password successfully",null));
            }
        }

        catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.error(e.getMessage(), HttpStatus.UNAUTHORIZED.value()));
        }
    }
    @PostMapping(value = "profile/uploadAvatar",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload avatar")
    public ResponseEntity<Object> uploadAvatar(HttpServletRequest request,@RequestPart(value = "file") MultipartFile file) {
       try{
           UserEntity userEntity = authenticationHandler.userAuthenticate(request);
           if(userEntity==null){
               throw new BadCredentialsException("User not found !");
           }
           if(!imageStorageService.isImageFile(file)){
               return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                       .body(ErrorResponse.error("File is not image", HttpStatus.UNPROCESSABLE_ENTITY.value()));
           }
           String url=imageStorageService.saveAvatar(file,userEntity.getEmail());
           if(url.equals("")){
               return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                       .body(ErrorResponse.error("Upload avatar failed", HttpStatus.UNPROCESSABLE_ENTITY.value()));
           }
           else{
                userEntity.setImg(url);
                userService.saveInfo(userEntity);
                HashMap<String,Object> data=new HashMap<>();
                data.put("img_url",url);
                return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(),"Upload avatar successfully",data));
           }
       }
       catch (Exception e){
              return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                     .body(ErrorResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
       }

    }
}
