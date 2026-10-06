package com.harates.product_management.model;

public class CartItem {

    private Product product;
    private int adet;
    private double birimFiyat;

    public CartItem(Product product, int adet) {
        this.product = product;
        this.adet = adet;
        this.birimFiyat = product.getFiyat();
    }

    public Product getProduct() {
        return product;
    }

    public int getAdet() {
        return adet;
    }

    public void setAdet(int adet) {
        this.adet = adet;
    }

    public double getBirimFiyat() {
        return birimFiyat;
    }

    public void setBirimFiyat(double birimFiyat) {
        this.birimFiyat = birimFiyat;
    }

    public double getToplamFiyat() {
        return birimFiyat * adet;
    }
}
