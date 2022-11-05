package com.abc.senki.controller;

import com.abc.senki.handler.AuthenticationHandler;
import com.abc.senki.model.entity.*;
import com.abc.senki.model.payload.request.CartRequest.AddCartItemRequest;
import com.abc.senki.model.payload.response.ErrorResponse;
import com.abc.senki.model.payload.response.SuccessResponse;
import com.abc.senki.service.CartService;
import com.abc.senki.service.ProductService;
import com.abc.senki.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    @Autowired
    AuthenticationHandler authenticationHandler;
    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;



    @GetMapping("/get")
    @Operation(summary = "Get cart by user")
    public ResponseEntity<Object> getCartByUser(HttpServletRequest request) {
        UserEntity user = authenticationHandler.userAuthenticate(request);
        if (user == null) {
            return ResponseEntity.badRequest().body(ErrorResponse.error("User not found", HttpStatus.BAD_REQUEST.value()));
        }
        Map<String, Object> data = new HashMap<>();
        CartEntity cart = user.getCart();
        //Add new cart if user don't have cart
        if (cart == null) {
            CartEntity newCart = new CartEntity(user);
            cartService.saveCart(newCart);
            data.put("cart", "");
            return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "Generate new cart", data));

        }
        //Get cart successfully
        data.put("cart", cart);
        return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "Get cart successfully", data));
    }
    @PutMapping("/add")
    @Operation(summary = "Add product to cart")
    public ResponseEntity<Object> addProductToCart(HttpServletRequest request, @RequestBody @Valid AddCartItemRequest addRequest) {
       try{
           UserEntity user = authenticationHandler.userAuthenticate(request);
           //        Handle null object error
           if (user == null) {
               return ResponseEntity.badRequest().body(ErrorResponse.error("User not found", HttpStatus.BAD_REQUEST.value()));
           }
           CartEntity cart = user.getCart();
           if(cart == null){
               return ResponseEntity.badRequest().body(ErrorResponse.error("Cart not found", HttpStatus.BAD_REQUEST.value()));
           }

           ProductEntity product = productService.findById(addRequest.getProductId());
           //Check if product already in cart
           if(cart.getCartItems().stream().filter(cartItem -> cartItem.getProduct().getId() == product.getId()).findFirst().isPresent()){
               return ResponseEntity.badRequest().body(ErrorResponse.error("Product already in cart", HttpStatus.BAD_REQUEST.value()));
           }
              //Check if product is null
           if(product == null){
               return ResponseEntity.badRequest().body(ErrorResponse.error("Product not found", HttpStatus.BAD_REQUEST.value()));
           }
           //          Check if there is a attribute value

           else{
               CartItemEntity cartItem=new CartItemEntity();
               cartItem.setInfo(cart,product,addRequest.getQuantity());
               cartService.saveCartItem(cartItem);
           }

           return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "Add product to cart successfully", null));
       }
         catch (Exception e){
              return ResponseEntity.badRequest().body(ErrorResponse.error("Add product to cart failed", HttpStatus.BAD_REQUEST.value()));
         }
    }
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete item from cart")
    public ResponseEntity<Object> deleteProductFromCart(HttpServletRequest request, @PathVariable("id") int id) {
      try{
          UserEntity user = authenticationHandler.userAuthenticate(request);
          //        Handle null object error
          if (user == null) {
              return ResponseEntity.badRequest().body(ErrorResponse.error("User not found", HttpStatus.BAD_REQUEST.value()));
          }
          CartEntity cart = user.getCart();
          if(cart == null){
              return ResponseEntity.badRequest().body(ErrorResponse.error("Cart not found", HttpStatus.BAD_REQUEST.value()));
          }
          CartItemEntity cartItem=cart.getCartItems().stream().filter(item->item.getId()==id).findFirst().orElse(null);
          if(cartItem==null){
              return ResponseEntity.badRequest().body(ErrorResponse.error("Cart item not found", HttpStatus.BAD_REQUEST.value()));
          }
          cartService.deleteCartItem(id);
          return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "Delete product from cart successfully", null));
      }
      catch (Exception e){
          return ResponseEntity.badRequest().body(ErrorResponse.error("Delete product from cart failed", HttpStatus.BAD_REQUEST.value()));
      }
    }
}
