package com.harates.product_management.model;

import java.util.Locale;

public class Product {
    private String ad;
    private double fiyat;
    private int stok;
    private double puan;

    public Product(String ad, double fiyat, int stok, double puan) {
        this.ad = ad;
        this.fiyat = fiyat;
        this.stok = stok;
        this.puan = puan;
    }

    public String getAd() {
        return ad;
    }

    public double getFiyat() {
        return fiyat;
    }

    public int getStok() {
        return stok;
    }

    public double getPuan() {
        return puan;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%s - Fiyat %.2f, Stok : %d, Değerlendirme : %.2f", ad, fiyat, stok, puan);
    }

}
