# BELL AI – Android proje taslağı

Bu proje, mevcut BELL AI web sayfasını Android WebView içinde açar ve kullanıcı isterse Android'in `SYSTEM_ALERT_WINDOW` izniyle diğer uygulamaların üzerinde kayan bir BELL AI penceresi başlatır.

## Özellikler
- Giriş/şifre ekranı yok.
- WebView adresi: https://amazing-cendol-b808f4.netlify.app
- Ana ekranda kayan pencere izni ve başlatma düğmeleri.
- Sürüklenebilir B simgesi; dokununca web sohbet paneli açılır.
- Kullanıcı Kapat düğmesiyle paneli kapatabilir.
- BELL AI neon simgesi uygulama ikonu olarak eklenmiştir.

## APK derleme (GitHub Actions)
1. Bu ZIP'i bilgisayarda veya GitHub Codespaces gibi bir geliştirme ortamında açıp içindeki dosyaları deponun köküne yükleyin. ZIP dosyasını tek başına depoya koymak yeterli değildir; içeriği açılmalıdır.
2. GitHub deposunda **Actions** sekmesini açın.
3. **Build BELL AI APK** iş akışını seçin ve **Run workflow** düğmesine basın.
4. İşlem bitince çalıştırma sayfasındaki **Artifacts** bölümünden `BELL-AI-debug-apk` dosyasını indirin.
5. ZIP içinden `app-debug.apk` dosyasını çıkarıp Android telefona kurun.

## Telefonda ilk kullanım
1. BELL AI uygulamasını açın.
2. **Kayan pencere izni** düğmesine basın.
3. Android ayarlarında BELL AI için **Diğer uygulamaların üzerinde göster** iznini etkinleştirin.
4. Uygulamaya geri dönün ve **Kayan pencereyi başlat** düğmesine basın.
5. B simgesi görünür. Oyuna geçince simgeye dokunarak BELL AI panelini açabilirsiniz.
6. Bitirince Android bildiriminden BELL AI hizmetini durdurun veya uygulamayı zorla durdurun.

## Önemli notlar
- Kayan pencere Android'in standart üstte gösterme iznini kullanır. Bazı oyunlar veya telefon yazılımları bu pencereyi engelleyebilir; oyun kuralları buna izin vermiyorsa kullanmayın. Bu proje oyun korumasını/anti-cheat'i aşmaya çalışmaz.
- Xiaomi/HyperOS sürümüne göre izin ekranlarının adları değişebilir. Gerekirse uygulama bilgileri içinden ek izinleri kontrol edin.
- Bu proje derlenebilir bir başlangıç taslağıdır; burada gerçek Android SDK ile APK derlemesi yapılmadı. GitHub Actions iş akışı APK'yı derlemek için eklendi. Derleme hatası çıkarsa hata metnine göre düzeltme gerekir.
- Site içindeki sohbet şu anda ZIP'teki web prototipinin kendi kurallar/anahtar kelime mantığına bağlıdır; bu proje kendi başına yeni bir yapay zekâ API'si veya gerçek kullanıcı doğrulaması eklemez.
- iOS için bu Android kayan pencere çözümü geçerli değildir.
