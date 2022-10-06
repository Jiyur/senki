package com.abc.senki.controller;

import com.abc.senki.handler.AuthenticationHandler;
import com.abc.senki.model.entity.AttributeEntity;
import com.abc.senki.model.entity.AttributeValueEntity;
import com.abc.senki.model.payload.request.AttributeRequest.NewAttributeRequest;
import com.abc.senki.model.payload.request.AttributeRequest.NewAttributeValueRequest;
import com.abc.senki.model.payload.response.ErrorResponse;
import com.abc.senki.model.payload.response.SuccessResponse;
import com.abc.senki.service.AttributeService;
import com.abc.senki.service.AttributeValueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@RequestMapping("/api/admin/attribute")
public class AdminAttributeController {
    @Autowired
    AuthenticationHandler authenticationHandler;
    @Autowired
    AttributeService attributeService;

    @Autowired
    AttributeValueService attributeValueService;


    @PostMapping("/insert")
    @Operation(summary = "add new attribute")
    public ResponseEntity<Object> insertAttribute(NewAttributeRequest attribute) {
        try {
            AttributeEntity attributeEntity = new AttributeEntity();
            attributeEntity.setName(attribute.getName());
            attributeService.saveAttribute(attributeEntity);
            return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "Insert attribute successfully", null));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ErrorResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
    @PostMapping("/value/insert")
    @Operation(summary = "add new attribute value")
    public ResponseEntity<Object> insertAttributeValue(String attributeId,@RequestBody List<NewAttributeValueRequest> valueList) {
        try {
            AttributeEntity attributeEntity = attributeService.findAttributeById(attributeId);
            for (NewAttributeValueRequest value : valueList) {
                AttributeValueEntity valueEntity=new AttributeValueEntity();
                valueEntity.setInfo(attributeEntity,value.getValue());
                attributeValueService.saveValue(valueEntity);
            }
            return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "Insert attribute successfully", null));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ErrorResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
    @GetMapping("/all")
    @Operation(summary = "Get all attribute")
    public ResponseEntity<Object> getAllAttribute() {
        try {
            List<AttributeEntity> attributeList = attributeService.getAllAttribute();
            if(attributeList.isEmpty()){
                return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "No attribute found", null));
            }
            Map<String,Object> data=new HashMap<>();
            data.put("attribute",attributeList);
            return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "Get all attribute successfully", data));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ErrorResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

}
