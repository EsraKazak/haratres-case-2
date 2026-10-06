package com.harates.product_management.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.harates.product_management.model.CartItem;
import com.harates.product_management.model.Product;
import com.harates.product_management.repository.ProductRepository;

@Service
public class CartService {

    private final ProductRepository productRepository;
    private final List<CartItem> sepet = new ArrayList<>();

    public CartService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public CartItem addToCart(String productName, int adet) {
        Product product = productRepository.findByName(productName);
        if (adet < 1) {
            throw new IllegalArgumentException("Adet en az 1 olmalıdır.");
        }
        if (product == null) {
            throw new IllegalArgumentException("Ürün bulunamadı: " + productName);
        }

        CartItem mevcut = findInCart(productName);
        int toplamAdet = (mevcut == null) ? adet : adet + mevcut.getAdet();

        if (toplamAdet > product.getStok()) {
            throw new IllegalArgumentException("Yeterli stok yok. Mevcut stok: " + product.getStok());
        }

        if (mevcut != null) {
            mevcut.setAdet(toplamAdet);
            return mevcut;
        }

        CartItem cartItem = new CartItem(product, adet);
        sepet.add(cartItem);
        return cartItem;
    }

    private CartItem findInCart(String ad) {
        for (CartItem item : sepet) {
            if (item.getProduct().getAd().equalsIgnoreCase(ad)) {
                return item;
            }
        }
        return null;
    }

    public List<CartItem> getCartItems() {
        return new ArrayList<>(sepet);
    }

    public int getCartSize() {
        return sepet.size();
    }

    public double calculateTotal() {
        if (sepet.size() < 2) {
            throw new IllegalStateException("Sepette en az 2 ürün olmalıdır.");
        }
        double toplam = 0;
        for (int i = 0; i < sepet.size(); i++) {
            CartItem item = sepet.get(i);
            double mevcutFiyat = sepet.get(i).getProduct().getFiyat();
            double yeniFiyat = mevcutFiyat;
            if (i < sepet.size() - 1) {
                double sonrakiFiyat = sepet.get(i + 1).getProduct().getFiyat();
                if (mevcutFiyat > sonrakiFiyat) {
                    yeniFiyat = mevcutFiyat - sonrakiFiyat;

                }

            }

            item.setBirimFiyat(yeniFiyat);
            toplam += item.getToplamFiyat();
        }
        return toplam;
    }

    public void clearCart() {
        sepet.clear();
    }
}
