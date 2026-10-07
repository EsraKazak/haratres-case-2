package com.harates.product_management.controller;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.harates.product_management.dto.CartItemRequest;
import com.harates.product_management.dto.CartItemResponse;
import com.harates.product_management.dto.CartResponse;
import com.harates.product_management.model.CartItem;
import com.harates.product_management.service.CartService;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    @PostMapping("/items")
    public CartItemResponse addItem(@RequestBody CartItemRequest req) {
        return CartItemResponse.from(service.addToCart(req.ad(), req.adet()));
    }

    @GetMapping
    public CartResponse getCart() {
        double toplamTutar = service.calculateTotal();
        List<CartItemResponse> items = service.getCartItems().stream().map(CartItemResponse::from).toList();
        return new CartResponse(items, toplamTutar);
    }

    @DeleteMapping
    public void clearCart() {
        service.clearCart();
    }

    @GetMapping("/size")
    public int getCartSize() {
        return service.getCartSize();
    }

    @GetMapping("/debug")
    public String debug() {
        return "Sepet boyutu: " + service.getCartSize()
                + " | Service hash: " + service.hashCode();
    }

}
