package com.harates.product_management.dto;

import java.util.List;

public record CartResponse(List<CartItemResponse> items, double toplamFiyat) {

}
