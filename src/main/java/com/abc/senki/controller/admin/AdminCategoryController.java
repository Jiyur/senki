package com.abc.senki.controller.admin;

import com.abc.senki.model.entity.CategoryEntity;
import com.abc.senki.model.payload.request.CategoryRequest.AddNewCategoryRequest;
import com.abc.senki.model.payload.request.CategoryRequest.EditCateRequest;
import com.abc.senki.model.payload.response.ErrorResponse;
import com.abc.senki.model.payload.response.SuccessResponse;
import com.abc.senki.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.checkerframework.checker.units.qual.A;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.UUID;

import static com.abc.senki.common.ErrorDefinition.ERROR_TRY_AGAIN;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/admin/category")
public class AdminCategoryController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    ModelMapper mapper;
    @GetMapping("")
    @Operation(summary = "Get all category")
    public ResponseEntity<Object> getAllCategory(){
        try{
            HashMap<String,Object> data=new HashMap<>();
            data.put("category",categoryService.findAll());
            return ResponseEntity
                    .ok(new SuccessResponse(HttpStatus.OK.value(),"Get all category successfully", data));
        }
        catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(ErrorResponse.error(ERROR_TRY_AGAIN.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }

    }
    @PostMapping("")
    @Operation(summary = "Insert category")
    public ResponseEntity<Object> addNewCategory(@RequestBody AddNewCategoryRequest request,@RequestParam(defaultValue = "00000000-0000-0000-0000-000000000000") UUID parentId){
        try{
            if(Boolean.TRUE.equals(categoryService.existsByName(request.getName()))){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ErrorResponse.error("Category name already exists", HttpStatus.CONFLICT.value()));
            }
            CategoryEntity categoryEntity=mapper.map(request,CategoryEntity.class);
            if(parentId.compareTo(UUID.fromString("00000000-0000-0000-0000-000000000000"))==0){
                categoryService.saveCategory(categoryEntity);
                return ResponseEntity.ok(new SuccessResponse( HttpStatus.OK.value(),"Add New Category Successfully",null));
            }
            if(categoryService.findById(String.valueOf(parentId))!=null){
                CategoryEntity parent=categoryService.findById(String.valueOf(parentId));
                categoryEntity.setParent(parent);
                categoryService.saveCategory(categoryEntity);
                return ResponseEntity.ok(new SuccessResponse( HttpStatus.OK.value(),"Add New Category Successfully",null));
            }
            else{
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ErrorResponse.error("Parent category not found", HttpStatus.CONFLICT.value()));
            }
        }
        catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(ErrorResponse.error(ERROR_TRY_AGAIN.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
    @PutMapping("{id}")
    @Operation(summary = "Update category")
    public ResponseEntity<Object> updateCategory(@PathVariable UUID id, @RequestBody EditCateRequest editCateRequest){
        try{
            CategoryEntity category=categoryService.findById(id.toString());
            if(category==null){
                return ResponseEntity.badRequest()
                        .body(ErrorResponse.error(ERROR_TRY_AGAIN.getMessage(), HttpStatus.BAD_REQUEST.value()));
            }
            if(editCateRequest.getParentCateId()!=null){
                category.setParent(categoryService.findById(editCateRequest.getParentCateId().toString()));
            }
            if(editCateRequest.getName()!=null){
                category.setName(editCateRequest.getName());
            }
            categoryService.saveCategory(category);
            return ResponseEntity
                    .ok(new SuccessResponse(HttpStatus.OK.value(),"Update category successfully", null));
        }
        catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(ErrorResponse.error(ERROR_TRY_AGAIN.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
}
