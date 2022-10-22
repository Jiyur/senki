package com.abc.senki.controller;

import com.abc.senki.handler.AuthenticationHandler;
import com.abc.senki.model.entity.*;
import com.abc.senki.model.payload.request.AddressRequest.InfoAddressRequest;
import com.abc.senki.model.payload.request.UserRequest.ChangePasswordRequest;
import com.abc.senki.model.payload.request.UserRequest.ChangePhoneRequest;
import com.abc.senki.model.payload.request.UserRequest.ChangeUserInfoRequest;
import com.abc.senki.model.payload.request.UserRequest.ForgetPasswordRequest;
import com.abc.senki.model.payload.response.ErrorResponse;
import com.abc.senki.model.payload.response.SuccessResponse;
import com.abc.senki.service.AddressService;
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
import java.util.List;

import static com.abc.senki.common.ErrorDefinition.*;

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
    private AddressService addressService;
    @Autowired
    AuthenticationHandler authenticationHandler;
    @Autowired
    ImageStorageService imageStorageService;


    @GetMapping("profile")
    @Operation(summary = "Get user profile")
    public ResponseEntity<Object> profile(HttpServletRequest request){
        UserEntity userEntity= authenticationHandler.userAuthenticate(request);
        if(userEntity==null){
            throw new BadCredentialsException(ACCESS_TOKEN_EXPIRED.getMessage());
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
        try{
            if(userEntity==null){
                throw new BadCredentialsException(ACCESS_TOKEN_EXPIRED.getMessage());
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
        catch (Exception e){
            return ResponseEntity.badRequest().body(ErrorResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
    @PutMapping("profile/changePhone")
    @Operation(summary = "Change user phone")
    public ResponseEntity<Object> changePhone(HttpServletRequest request, @RequestBody @Valid ChangePhoneRequest user) {
        UserEntity userEntity = authenticationHandler.userAuthenticate(request);
        try{
            if(userEntity==null){
                throw new BadCredentialsException(ACCESS_TOKEN_EXPIRED.getMessage());
            }
            else{
                userEntity.setPhone(user.getPhone());
                userService.saveInfo(userEntity);
                HashMap<String,Object> data=new HashMap<>();
                data.put("user",user.getPhone());
                return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(),"Change phone number successfully",data));
            }
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(ErrorResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
    @PutMapping("profile/changeEmail")
    @Operation(summary = "Change user email")
    public ResponseEntity<Object> changeEmail(HttpServletRequest request,@RequestBody @Valid ForgetPasswordRequest user) {
        UserEntity userEntity = authenticationHandler.userAuthenticate(request);
        try{
            if(userEntity==null){
                throw new BadCredentialsException(ACCESS_TOKEN_EXPIRED.getMessage());
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
        catch (Exception e){
            return ResponseEntity.badRequest().body(ErrorResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
    @PutMapping("/profile/changePassword")
    @Operation(summary = "Change user password")
    public ResponseEntity<Object> changePassword(HttpServletRequest req, @RequestBody @Valid ChangePasswordRequest request) throws BadCredentialsException{
        UserEntity user=authenticationHandler.userAuthenticate(req);
        try{
            if(user==null){
                throw new BadCredentialsException(USER_NOT_FOUND.getMessage());
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
               throw new BadCredentialsException(USER_NOT_FOUND.getMessage());
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
    @GetMapping("address")
    @Operation(summary = "get user address")
    public ResponseEntity<Object> getUserAddress(HttpServletRequest request) {
        try {
            UserEntity userEntity = authenticationHandler.userAuthenticate(request);
            if(userEntity==null){
                throw new BadCredentialsException(USER_NOT_FOUND.getMessage());
            }
            List<AddressEntity> list=userEntity.getAddress();
            HashMap<String,Object> data=new HashMap<>();
            data.put("address",list);

            return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "Get address successfully", data));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ErrorResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
    @PostMapping("address/save")
    @Operation(summary = "save user address")
    public ResponseEntity<Object> saveUserAddress(HttpServletRequest request,@RequestBody @Valid InfoAddressRequest address) {
        try {
            UserEntity userEntity = authenticationHandler.userAuthenticate(request);
            if(userEntity==null){
                throw new BadCredentialsException(USER_NOT_FOUND.getMessage());
            }
            AddressEntity addressEntity=new AddressEntity();
            ProvinceEntity provinceEntity=addressService.getProvinceById(address.getProvince());
            DistrictEntity districtEntity=addressService.getDistrictById(address.getDistrict());
            CommuneEntity communeEntity=addressService.getCommuneById(address.getCommune());
            if(provinceEntity==null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ErrorResponse.error("Province is not exist", HttpStatus.BAD_REQUEST.value()));
            }
            if(districtEntity==null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ErrorResponse.error("District is not exist", HttpStatus.BAD_REQUEST.value()));
            }
            if(communeEntity==null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ErrorResponse.error("Commune is not exist", HttpStatus.BAD_REQUEST.value()));
            }
            addressEntity.setProvince(provinceEntity);
            addressEntity.setDistrict(districtEntity);
            addressEntity.setCommune(communeEntity);
            addressEntity.setInfo(address.getFullName()
                    ,address.getCompanyName()
                    ,address.getPhone()
                    ,address.getAddressDetail()
                    ,userEntity);
            addressService.saveAddress(addressEntity);

            return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "Save address successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ErrorResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
    @DeleteMapping("address/delete/{id}")
    @Operation(summary = "delete user address")
    public ResponseEntity<Object> deleteUserAddress(HttpServletRequest request,@PathVariable("id") String id) {
        try {
            UserEntity userEntity = authenticationHandler.userAuthenticate(request);
            if(userEntity==null){
                throw new BadCredentialsException(USER_NOT_FOUND.getMessage());
            }
            addressService.deleteAddress(id);
            return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "Delete address successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ErrorResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
}
