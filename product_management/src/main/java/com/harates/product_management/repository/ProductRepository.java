package com.harates.product_management.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.harates.product_management.model.Product;
import java.util.ArrayList;

@Repository
public class ProductRepository {

    private List<Product> products = new ArrayList<Product>();

    public void save(Product product) {
        products.add(product);
    }

    // kopyayı dondürüyoruz ki asıl liste bozulmasın
    public List<Product> findAll() {
        return new ArrayList<>(products);
    }

    public Product findByName(String name) {
        for (Product product : products) {
            if (product.getAd().equalsIgnoreCase(name)) {
                return product;
            }
        }
        return null;
    }

    public boolean existsByName(String name) {
        return findByName(name) != null;
    }

    public int count() {
        return products.size();
    }

}
