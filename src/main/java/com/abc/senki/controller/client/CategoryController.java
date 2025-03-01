package com.abc.senki.controller.client;

import com.abc.senki.model.payload.response.ErrorResponse;
import com.abc.senki.model.payload.response.SuccessResponse;
import com.abc.senki.service.CategoryService;
import com.abc.senki.util.DataUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

import static com.abc.senki.common.ErrorDefinition.ERROR_TRY_AGAIN;

@RestController
@AllArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("")
    @Operation(summary = "get all category")
    public ResponseEntity<Object> getAllCategory() {
        try{
            return ResponseEntity
                    .ok(new SuccessResponse(HttpStatus.OK.value(),"Get all category successfully",
                            DataUtil.getData("category",categoryService.findAll())));
        }
        catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(ErrorResponse.error(ERROR_TRY_AGAIN.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
    @GetMapping("parent")
    @Operation(summary = "get all parent category")
    public ResponseEntity<Object> getAllParentCategory() {
        try{
            return ResponseEntity
                    .ok(new SuccessResponse(HttpStatus.OK.value(),"Get all category successfully",
                            DataUtil.getData("category",categoryService.findAllParent())));
        }
        catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(ErrorResponse.error(ERROR_TRY_AGAIN.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

}
