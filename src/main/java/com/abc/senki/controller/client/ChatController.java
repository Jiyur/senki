package com.abc.senki.controller.client;

import com.abc.senki.model.entity.UserEntity;
import com.abc.senki.model.payload.response.SuccessResponse;
import com.abc.senki.service.UserService;
import com.abc.senki.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/chat/")
public class ChatController {
    @Autowired
    private UserService userService;

    @GetMapping("seller/list")
    public ResponseEntity<Object> getSeller(@RequestParam("nickname") String nickname) {
        List<UserEntity> sellerList=userService.getSellerByNickname(nickname);
        return ResponseEntity.ok(new SuccessResponse("Get list seller success",
                DataUtil.getData("sellerList",sellerList)));
    }
}
