package com.harates.product_management.dto;

import com.harates.product_management.model.CartItem;

public record CartItemResponse(String ad, int adet, double birimFiyat, double toplamFiyat) {
    public static CartItemResponse from(CartItem c) {
        return new CartItemResponse(c.getProduct().getAd(), c.getAdet(), c.getBirimFiyat(), c.getToplamFiyat());
    }

}
