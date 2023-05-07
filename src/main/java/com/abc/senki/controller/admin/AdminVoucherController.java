package com.abc.senki.controller.admin;

import com.abc.senki.handler.BadRequestException;
import com.abc.senki.model.entity.VoucherEntity;
import com.abc.senki.model.payload.request.VoucherRequest.AddVoucher;
import com.abc.senki.model.payload.response.SuccessResponse;
import com.abc.senki.service.VoucherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("/admin/voucher")
@SecurityRequirement(name = "bearerAuth")
public class AdminVoucherController {
    @Autowired
    ModelMapper mapper;
    @Autowired
    private VoucherService voucherService;
    @GetMapping("/")
    @Operation(summary = "Get all vouchers")
    public ResponseEntity<Object> getAllVouchers(@RequestParam(value = "page",defaultValue = "0") int page,
                                                 @RequestParam(value = "size",defaultValue = "6") int size){
        try{
            //Sort voucher by end date
            Sort sort=Sort.by(Sort.Direction.DESC,"endDate");
            HashMap<String,Object> data=new HashMap<>();
            //Init pageable
            Pageable pageable= PageRequest.of(page,size,sort);
            //Get all vouchers & response
            data.put("vouchers",voucherService.getAllVoucher(pageable));
            return ResponseEntity.ok(
                    new SuccessResponse(HttpStatus.OK.value(),"Get all vouchers successfully",data));
        }
        catch (Exception e){
            throw new BadRequestException(e.getMessage());

        }
    }
    @DeleteMapping("{id}")
    @Operation(summary = "Delete voucher by id")
    public ResponseEntity<Object> deleteVoucher(@PathVariable("id") UUID id){
        try{
            voucherService.deleteVoucher(id);
            return ResponseEntity.ok(
                    new SuccessResponse(HttpStatus.OK.value(),"Delete voucher successfully",null));
        }
        catch (Exception e){
            throw new BadRequestException(e.getMessage());

        }
    }
    @PostMapping("/")
    @Operation(summary = "Add new voucher")
    public ResponseEntity<Object> addVoucher(@RequestBody AddVoucher request){
        try{
            //Set voucher end date after 7 days
            request.setEndDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).plusDays(7));
            //Map request to voucher entity and save
            voucherService.saveVoucher(mapper.map(request, VoucherEntity.class));
            //Response
            return ResponseEntity.ok(
                    new SuccessResponse(HttpStatus.OK.value(),"Add voucher successfully",null));
        }
        catch (Exception e){
            throw new BadRequestException(e.getMessage());
        }
    }

}
