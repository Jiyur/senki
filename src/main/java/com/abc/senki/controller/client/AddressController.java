package com.abc.senki.controller.client;


import com.abc.senki.model.entity.CommuneEntity;
import com.abc.senki.model.entity.DistrictEntity;
import com.abc.senki.model.entity.ProvinceEntity;
import com.abc.senki.model.payload.response.ErrorResponse;
import com.abc.senki.model.payload.response.SuccessResponse;
import com.abc.senki.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/address")
public class AddressController {
    private final AddressService addressService;


    @GetMapping("/province/all")
    @Operation(summary = "get all province")
    public ResponseEntity<Object> getAllProvince() {

        try
        {
            List<ProvinceEntity> list = addressService.getAllProvince();
            if(list.isEmpty())
            {
                return ResponseEntity.badRequest().body(ErrorResponse.error("No province found", HttpStatus.BAD_REQUEST.value()));
            }
            Map<String, Object> data = new HashMap<>();
            data.put("province", list);
            return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "Get all province successfully", data));
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(ErrorResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }


    @GetMapping("/district/{id}")
    @Operation(summary = "get district by province id")
    public ResponseEntity<Object> getDistrictByProvinceId(@PathVariable("id") String id) {

        try
        {
            List<DistrictEntity> list = addressService.getAllDistrictInProvince(id);
            if(list.isEmpty())
            {
                return ResponseEntity.badRequest().body(ErrorResponse.error("No district found", HttpStatus.BAD_REQUEST.value()));
            }
            Map<String, Object> data = new HashMap<>();
            data.put("district", list);
            return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "Get all district successfully", data));
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(ErrorResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
    @GetMapping("/commune/{id}")
    @Operation(summary = "get commune by district id")
    public ResponseEntity<Object> getCommuneByDistrictId(@PathVariable("id") String id) {

        try
        {
            List<CommuneEntity> list = addressService.getAllCommuneInDistrict(id);
            if(list.isEmpty())
            {
                return ResponseEntity.badRequest().body(ErrorResponse.error("No commune found", HttpStatus.BAD_REQUEST.value()));
            }
            Map<String, Object> data = new HashMap<>();
            data.put("commune", list);
            return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "Get all commune successfully", data));
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(ErrorResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
    @GetMapping("/province/get/{id}")
    @Operation(summary = "get province by id")
    public ResponseEntity<Object> getProvinceById(@PathVariable("id") String id) {

        try
        {
            ProvinceEntity provinceEntity = addressService.getProvinceById(id);
            if(provinceEntity == null)
            {
                return ResponseEntity.badRequest().body(ErrorResponse.error("No province found", HttpStatus.BAD_REQUEST.value()));
            }
            Map<String, Object> data = new HashMap<>();
            data.put("province", provinceEntity);
            return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "Get province successfully", data));
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(ErrorResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
    @GetMapping("/district/get/{id}")
    @Operation(summary = "get district by id")
    public ResponseEntity<Object> getDistrictById(@PathVariable("id") String id) {

        try
        {
            DistrictEntity districtEntity = addressService.getDistrictById(id);
            if(districtEntity == null)
            {
                return ResponseEntity.badRequest().body(ErrorResponse.error("No district found", HttpStatus.BAD_REQUEST.value()));
            }
            Map<String, Object> data = new HashMap<>();
            data.put("district", districtEntity);
            return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "Get district successfully", data));
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(ErrorResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
    @GetMapping("/commune/get/{id}")
    @Operation(summary = "get commune by id")
    public ResponseEntity<Object> getCommuneById(@PathVariable("id") String id) {

        try
        {
            CommuneEntity communeEntity = addressService.getCommuneById(id);
            if(communeEntity == null)
            {
                return ResponseEntity.badRequest().body(ErrorResponse.error("No commune found", HttpStatus.BAD_REQUEST.value()));
            }
            Map<String, Object> data = new HashMap<>();
            data.put("commune", communeEntity);
            return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "Get commune successfully", data));
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(ErrorResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
}
