package com.harates.product_management.dto;

public record ProductRequest(
        String ad,
        double fiyat,
        int stok,
        double puan) {

}
