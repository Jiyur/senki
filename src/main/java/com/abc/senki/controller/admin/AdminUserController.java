package com.abc.senki.controller.admin;

import com.abc.senki.handler.AuthenticationHandler;
import com.abc.senki.model.entity.UserEntity;
import com.abc.senki.model.payload.response.ErrorResponse;
import com.abc.senki.model.payload.response.SuccessResponse;
import com.abc.senki.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@SecurityRequirement(name = "bearerAuth")
public class AdminUserController {
    @Autowired
    AuthenticationHandler authenticationHandler;
    @Autowired
    private UserService userService;
    @GetMapping("user")
    @Operation(summary = "Get all user")
    public ResponseEntity<Object> listAll(){
        List<UserEntity> list;
        try{
            list=userService.getAllUser();
            if(list.isEmpty()){
                return ResponseEntity.badRequest()
                        .body(ErrorResponse.error("No user found", HttpStatus.BAD_REQUEST.value()));
            }
            else{
                HashMap<String,Object> data=new HashMap<>();
                data.put("users",list);
                return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(),"Get all user successfully",data));
            }
        }
        catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(ErrorResponse.error("Error! Please try again later",HttpStatus.BAD_REQUEST.value()));
        }


    }
    @DeleteMapping("user/{id}")
    @Operation(summary = "Delete user")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") String id){
        try{
            userService.deleteById(id);
            return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(),"Delete user successfully",null));
        }
        catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(ErrorResponse.error(e.toString(),HttpStatus.BAD_REQUEST.value()));
        }
    }
    @PatchMapping("user/disable/{id}")
    @Operation(summary = "Disable user")
    public ResponseEntity<Object> disableUser(@PathVariable("id") String id){
        try{
            userService.updateActive(userService.findById(UUID.fromString(id)),false);
            return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(),"Disable user successfully",null));
        }
        catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(ErrorResponse.error(e.toString(),HttpStatus.BAD_REQUEST.value()));
        }
    }
    @PatchMapping("user/enable/{id}")
    @Operation(summary = "Enable user")
    public ResponseEntity<Object> enableUser(@PathVariable("id") String id){
        try{
            userService.updateActive(userService.findById(UUID.fromString(id)),true);
            return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(),"Enable user successfully",null));
        }
        catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(ErrorResponse.error(e.toString(),HttpStatus.BAD_REQUEST.value()));
        }
    }


}
