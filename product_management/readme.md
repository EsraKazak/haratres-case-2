# Ürün Yönetim Sistemi

Spring Boot ile geliştirilmiş dinamik ürün yönetim sistemi. Konsol ve REST API olmak üzere iki modda çalışır.

---

## Gereksinimler

- Java 17 veya üstü
- Maven (proje içinde `mvnw.cmd` ile gelir, ayrıca kurulum gerekmez)

Kurulu olup olmadığını kontrol etmek için terminalde:

java -version

---

## Proje Yapısı

src/main/java/com/harates/product_management/
├── model/
│   ├── Product.java          → Ürün veri sınıfı
│   └── CartItem.java         → Sepet kalemi veri sınıfı
├── repository/
│   └── ProductRepository.java → Bellekte ürün deposu
├── service/
│   ├── ProductService.java   → Ürün iş mantığı ve doğrulama
│   ├── SortService.java      → Sıralama iş mantığı
│   └── CartService.java      → Sepet iş mantığı ve indirim hesabı
├── dto/
│   ├── ProductRequest.java   → Ürün ekleme isteği
│   ├── ProductResponse.java  → Ürün yanıtı
│   ├── CartItemRequest.java  → Sepete ekleme isteği
│   ├── CartItemResponse.java → Sepet kalemi yanıtı
│   └── CartResponse.java     → Sepet yanıtı
├── controller/
│   ├── ProductController.java       → Ürün REST uç noktaları
│   ├── CartController.java          → Sepet REST uç noktaları
│   └── GlobalExceptionHandler.java  → Merkezi hata yönetimi
└── runner/
    └── ConsoleRunner.java    → Konsol modu akışı

---

## Çalıştırma

### Konsol Modu

şu komutu çalıştır:

**Terminalde:**
\.calistir.ps1


calistir.ps1 dosyası şunları otomatik yapar:
- Terminali UTF-8 moduna alır (Türkçe karakter desteği)
- Konsol profilini aktif eder
- Uygulamayı başlatır

### REST API Modu

terminalde:

.\mvnw.cmd spring-boot:run

Uygulama http://localhost:8080 adresinde ayağa kalkar.


---

## REST API Uç Noktaları

### Ürün İşlemleri

#### Ürün ekle
POST /api/v1/products
Content-Type: application/json

{
  "ad": "Defter",
  "fiyat": 3.0,
  "stok": 50,
  "puan": 4.7
}

#### Ürünleri listele (isteğe bağlı sıralama)
GET /api/v1/products?sortBy=puan&order=azalan

| Parametre | Varsayılan | Seçenekler |
|---|---|---|
| sortBy | ad | ad, name, fiyat, price, stok, stock, puan, rating |
| order | artan | artan, asc, azalan, desc |

### Sepet İşlemleri

#### Sepete ürün ekle
POST /api/v1/cart/items
Content-Type: application/json

{
  "ad": "Defter",
  "adet": 2
}

#### Sepeti görüntüle
GET /api/v1/cart

Örnek yanıt:
{
  "items": [
    {
      "ad": "Defter",
      "adet": 2,
      "birimFiyat": 1.5,
      "toplamFiyat": 3.0
    },
    {
      "ad": "Kalem",
      "adet": 2,
      "birimFiyat": 1.5,
      "toplamFiyat": 3.0
    }
  ],
  "toplamFiyat": 6.0
}

#### Sepeti boşalt
DELETE /api/v1/cart

---

## Doğrulama Kuralları

| Alan | Kural |
|---|---|
| Ürün adı | Boş olamaz, en fazla 20 karakter, benzersiz olmalı |
| Fiyat | 1 ile 100 arasında (sınırlar dahil) |
| Stok | En az 1 |
| Puan | 0 ile 5 arasında |
| Sepet adedi | En az 1 |
| Sepet | En az 2 farklı ürün kalemi içermeli |

Doğrulama hatalarında REST API 400 Bad Request ve hata mesajı döndürür.

---

## İndirim Hesabı

Sepetteki ürünler eklenme sırasına göre karşılaştırılır. Bir ürünün birim fiyatı kendinden sonraki üründen yüksekse, o üründen sonraki ürünün birim fiyatı kadar indirim yapılır.

Formül: yeniBirimFiyat = mevcutBirimFiyat - sonrakiÜrününBirimFiyatı

Örnek:

| Ürün | Adet | Birim Fiyat | Toplam |
|---|---|---|---|
| Defter | 2 | 3.00 | 6.00 |
| Kalem | 2 | 1.50 | 3.00 |
| Toplam | | | 9.00 |

İndirim sonrası (Defter 3.00 > Kalem 1.50, fark 1.50 indirim):

| Ürün | Adet | İndirimli Birim Fiyat | Toplam |
|---|---|---|---|
| Defter | 2 | 1.50 | 3.00 |
| Kalem | 2 | 1.50 | 3.00 |
| Toplam | | | 6.00 |

---

## Tasarım Kararları

| Karar | Açıklama |
|---|---|
| Aynı ürünü tekrar sepete ekleme | Yeni satır açılmaz, mevcut kalemin adedi artar. Stok kontrolü toplam adede göre yapılır. |
| Hata durumunda konsol davranışı | Geçersiz değer girilirse ürünün tüm alanları baştan istenir. |
| İndirim hesabı zamanı | Kullanıcı sepeti tamamlayınca hesaplanır, ekleme sırasında değil. |
| Karşılaştırma fiyatı | Her zaman ürünün orijinal fiyatı kullanılır. calculateTotal() birden fazla çağrılsa aynı sonucu verir. |
| Stok düşme | Sepete ekleme stoktan düşmez, sadece kontrol eder. |
| Kalıcılık | Ürünler ve sepet bellekte tutulur. Uygulama yeniden başlatılınca sıfırlanır. |

---
