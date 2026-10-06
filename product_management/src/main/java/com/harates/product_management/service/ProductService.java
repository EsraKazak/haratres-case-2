package com.harates.product_management.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.harates.product_management.model.Product;
import com.harates.product_management.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private static final int MAX_AD_UZUNLUK = 20;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product addProduct(String ad, double fiyat, int stok, double puan) {
        if (ad == null || ad.isBlank()) {
            throw new IllegalArgumentException("Ürün adı boş olamaz.");
        }
        if (productRepository.existsByName(ad)) {
            throw new IllegalArgumentException("Ürün adı benzersiz olmalıdır.");
        }
        if (ad.length() > MAX_AD_UZUNLUK) {
            throw new IllegalArgumentException("Ürün adı 20 karakterden uzun olamaz.");
        }
        if (stok < 1) {
            throw new IllegalArgumentException("Ürün stoku en az 1 olmalıdır.");
        }
        if (fiyat < 1 || fiyat > 100) {
            throw new IllegalArgumentException("Ürün fiyatı 1 ile 100 arasında olmalıdır.");
        }
        if (puan < 0 || puan > 5) {
            throw new IllegalArgumentException("Ürün puanı 0 ile 5 arasında olmalıdır.");
        }

        Product product = new Product(ad, fiyat, stok, puan);
        productRepository.save(product);
        return product;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public int getProductCount() {
        int count = productRepository.count();
        return count;
    }
}
