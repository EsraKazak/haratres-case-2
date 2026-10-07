package com.harates.product_management.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.harates.product_management.dto.ProductRequest;
import com.harates.product_management.dto.ProductResponse;
import com.harates.product_management.model.Product;
import com.harates.product_management.service.ProductService;
import com.harates.product_management.service.SortService;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService service;
    private final SortService sortService;

    public ProductController(ProductService service, SortService sortService) {
        this.service = service;
        this.sortService = sortService;
    }

    @PostMapping
    public ProductResponse add(@RequestBody ProductRequest req) {
        Product p = service.addProduct(req.ad(), req.fiyat(), req.stok(), req.puan());
        return ProductResponse.from(p);
    }

    @GetMapping
    public List<ProductResponse> list(
            @RequestParam(defaultValue = "ad") String sortBy,
            @RequestParam(defaultValue = "asc") String order) {
        boolean artan = order.equalsIgnoreCase("asc");
        List<Product> sirali = sortService.sirala(service.getAllProducts(), sortBy, artan);
        return sirali.stream().map(ProductResponse::from).toList();
    }

}
