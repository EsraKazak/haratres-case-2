package com.harates.product_management.dto;

import com.harates.product_management.model.Product;

public record ProductResponse(
        String ad,
        double fiyat,
        int stok,
        double puan) {

    public static ProductResponse from(Product p) {
        return new ProductResponse(
                p.getAd(),
                p.getFiyat(),
                p.getStok(),
                p.getPuan());
    }
}
