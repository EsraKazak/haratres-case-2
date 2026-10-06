package com.harates.product_management.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.harates.product_management.model.Product;

@Service
public class SortService {

    public List<Product> sirala(List<Product> products, String kriter, boolean artan) {
        List<Product> kopya = new ArrayList<>(products);
        Comparator<Product> kural;

        switch (kriter.toLowerCase()) {
            case "ad":
                kural = Comparator.comparing(Product::getAd, String.CASE_INSENSITIVE_ORDER);
                break;
            case "fiyat":
                kural = Comparator.comparingDouble(Product::getFiyat);
                break;
            case "stok":
                kural = Comparator.comparingInt(Product::getStok);
                break;
            case "puan":
                kural = Comparator.comparingDouble(Product::getPuan);
                break;
            default:
                throw new IllegalArgumentException("Geçersiz sıralama kriteri: " + kriter);
        }

        if (!artan) {
            kural = kural.reversed();
        }

        kopya.sort(kural);
        return kopya;
    }
}
