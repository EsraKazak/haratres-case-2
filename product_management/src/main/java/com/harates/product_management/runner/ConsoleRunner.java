package com.harates.product_management.runner;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.harates.product_management.model.CartItem;
import com.harates.product_management.model.Product;
import com.harates.product_management.service.CartService;
import com.harates.product_management.service.ProductService;
import com.harates.product_management.service.SortService;

@Component
public class ConsoleRunner implements CommandLineRunner {

    private final ProductService productService;
    private final SortService sortService;
    private final CartService cartService;

    private final Scanner scanner = new Scanner(System.in);

    public ConsoleRunner(ProductService productService, SortService sortService, CartService cartService) {
        this.productService = productService;
        this.sortService = sortService;
        this.cartService = cartService;
    }

    @Override
    public void run(String... args) throws Exception {
        urunleriAl();
        urunleriSirala();
        sepetAkisi();
    }

    private int okuInt(String mesaj) {
        while (true) {
            System.out.print(mesaj);
            String string = scanner.nextLine();
            try {
                return Integer.parseInt(string.trim());
            } catch (NumberFormatException e) {
                System.out.print("Lütfen geçerli bir sayı girin.");
            }
        }
    }

    private double okuDouble(String mesaj) {
        while (true) {
            System.out.print(mesaj);
            String string = scanner.nextLine();
            try {
                return Double.parseDouble(string.trim().replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.print("Lütfen geçerli bir sayı girin.");
            }

        }
    }

    private String okuString(String mesaj) {
        System.out.print(mesaj);
        String string = scanner.nextLine().trim();
        return string;
    }

    private void urunleriAl() {
        int sayi;
        while (true) {
            sayi = okuInt("Kaç farklı ürün gireceksiniz: ");
            if (sayi < 2) {
                System.out.println("ürün 2 den az olamaz");
            } else {
                break;
            }
        }
        for (int i = 1; i <= sayi; i++) {
            System.out.println("ürün " + i + ":");
            while (true) {
                String ad = okuString("ürün Adı:");
                double fiyat = okuDouble("Birim fiyatı:");
                int stok = okuInt("Stok Miktarı:");
                double puan = okuDouble("Değerlendirme Puanı:");

                try {
                    productService.addProduct(ad, fiyat, stok, puan);
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println("Hata: " + e.getMessage() + " Lütfen tekrar girin.");
                }
            }
        }
    }

    private void urunleriSirala() {
        boolean artan;
        while (true) {
            String cevap = okuString("Sıralama türü artan mı azalan mı olsun? (artan/azalan):").toLowerCase()
                    .replace('ı', 'i');
            ;
            if (cevap.equalsIgnoreCase("artan")) {
                artan = true;
                break;
            }
            if (cevap.equalsIgnoreCase("azalan")) {
                artan = false;
                break;
            }
            System.out.println("Lütfen Artan ya da Azalan yazın):).");

        }
        List<Product> urunListe = productService.getAllProducts();
        while (true) {
            String kriter = okuString("Ürünleri hangi kritere göre sıralamak istersiniz?(ad,fiyat,stok,puan):");

            try {
                List<Product> sirali = sortService.sirala(urunListe, kriter, artan);
                for (Product p : sirali) {
                    System.out.println(p);
                }
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Hata: " + e.getMessage());
            }
        }

    }

    private void sepetAkisi() {
        boolean ilkMi = true;
        while (true) {
            String soru = ilkMi
                    ? "Sepete ürün eklemek ister misiniz? (Evet/Hayır): "
                    : "Sepete başka ürün eklemek ister misiniz? (Evet/Hayır): ";
            String cevap = okuString(soru);
            if (cevap.equalsIgnoreCase("hayır") || cevap.equalsIgnoreCase("hayir")) {
                if (cartService.getCartSize() < 2) {
                    System.out.println("En az 2 ürün almalısınızı");
                    continue;
                }
                break;
            }

            if (!cevap.equalsIgnoreCase("evet")) {
                System.out.println("Lütfen Evet ya da Hayır Yazın");
                continue;
            }

            while (true) {
                String ad = okuString("Eklemek istediğiniz ürünün adı:");
                int adet = okuInt("Eklemek istediğiniz adet:");
                try {

                    CartItem item = cartService.addToCart(ad, adet);
                    System.out.println(item.getProduct().getAd() + "Sepetinize eklendi.");
                    break;

                } catch (IllegalArgumentException e) {
                    System.out.println("Hata: " + e.getMessage());
                }
            }
        }

        double toplam = cartService.calculateTotal();
        System.out.print("Sepetiniz :");
        for (CartItem item : cartService.getCartItems()) {
            System.out.println(item.getProduct().getAd() + "- Adet: " + item.getAdet() + ", Toplam Fiyat: "
                    + String.format(Locale.US, "%.2f", item.getToplamFiyat()));
        }

        System.out.println("Sepet Toplamı: " + String.format(Locale.US, "%.2f", toplam));
    }
}
