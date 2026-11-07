# SMART ROOM MONITORING SYSTEM
> **Tugas Responsi Akhir Praktikum Pemrograman Berorientasi Objek (PBO)**  
> **Jurusan Informatika, Fakultas Teknik, Universitas Jenderal Soedirman**

---

## 1. Identitas Mahasiswa
Silakan lengkapi data diri Anda di bawah ini:

- **Nama Lengkap** : ALBERT NESTOR JULIART
- **NIM**          : H1D025134
- **Kelas/Praktikum**: C
- **Program Studi**: S1 Informatika
- **Fakultas**      : Teknik
- **Universitas**   : Universitas Jenderal Soedirman 

---

## 2. Deskripsi Sistem
**Smart Room Monitoring System** adalah aplikasi konsol (Java Console Application) berbasis simulasi **Internet of Things (IoT)** yang dirancang untuk memantau suhu ruangan dan mengendalikan perangkat aktuator (kipas pintar) secara otomatis maupun manual.

Sistem bekerja berdasarkan aturan ambang batas suhu:
- **Suhu $\ge$ 30.0°C** : Sistem mendeteksi kondisi **PANAS**, dan kipas pintar akan secara **otomatis aktif** (kecepatan sedang/level 2).
- **Suhu < 30.0°C**  : Sistem mendeteksi kondisi **NORMAL**, dan kipas pintar akan secara **otomatis nonaktif** (standby/level 0).
- **Kontrol Manual** : Pengguna dapat menyalakan atau mematikan kipas kapan saja melalui menu manual tanpa terikat kondisi suhu otomatis.

Aplikasi ini murni berbasis Java Standard Edition (SE) tanpa memerlukan perangkat keras fisik (ESP32/Arduino), menjadikannya ringkas, andal, dan sangat cocok untuk menguji pemahaman konsep fundamental Object-Oriented Programming (OOP).

---

## 3. Tema Project
- **Tema**: Internet of Things (IoT) 
- **Nama Perangkat**:
  - `SensorSuhu` (IoT Sensor Unit)
  - `KipasPintar` (IoT Actuator Unit)

---

## 4. Fitur Utama Program
1. **Input & Pengolahan Data Suhu**: Membaca input suhu ruangan dari konsol terminal dengan validasi tipe data dan rentang nilai.
2. **Deteksi Kondisi Ruangan Otomatis**: Mendeteksi status ruangan (`PANAS` vs `NORMAL`) berdasarkan ambang batas `30.0°C`.
3. **Otomasi Aktuator Kipas**: Kipas menyala otomatis saat suhu panas dan mati saat suhu normal.
4. **Kendali Manual Aktuator**: Pengguna dapat menyalakan (`ON`) dan mematikan (`OFF`) kipas secara manual melalui antarmuka pengendali.
5. **Pemantauan Status Real-Time**: Menampilkan ringkasan status ruangan, suhu terakhir, kondisi, mode operasi, dan kecepatan kipas.
6. **Informasi Lengkap Perangkat**: Menampilkan identitas (ID perangkat, nama, status daya, atribut khusus) dari sensor dan aktuator.
7. **Pencegahan Error / Validasi Input**: Sistem kebal terhadap input selain angka (misal huruf/simbol) tanpa mengalami crash.

---

## 5. Struktur File Project
```text
IoT_Smart_Monitoring/
├── PerangkatIoT.java      # Abstract Superclass (Dasar seluruh perangkat IoT)
├── SensorSuhu.java        # Subclass PerangkatIoT (Sensor pembaca suhu)
├── KipasPintar.java       # Subclass PerangkatIoT & Implementasi Multiple Interface
├── BisaDimonitor.java     # Interface untuk kemampuan monitoring perangkat
├── BisaDikendalikan.java  # Interface untuk kemampuan kendali daya perangkat
├── Main.java              # Entry point aplikasi konsol interaktif
├── TestOOP.java           # Program pengujian otomatis untuk verifikasi 10 requirement PBO
├── run.bat                # Shortcut 1-klik untuk compile & jalankan Main di Windows
├── test.bat               # Shortcut 1-klik untuk compile & jalankan TestOOP di Windows
├── README.md              # Dokumentasi lengkap proyek
├── DOKUMENTASI_PEMBELAJARAN.md # Materi persiapan responsi/tes lisan dosen & 25 Q&A
└── screenshots/           # Folder penyimpanan screenshot pengujian program
```

---

## 6. Pemetaan Penerapan 10 Requirement OOP

Berikut adalah bukti konkret penerapan ke-10 persyaratan wajib OOP di dalam kode sumber:

| No | Requirement PBO | Lokasi dalam Kode Sumber | Penjelasan Singkat |
|---|---|---|---|
| **1** | **State dan Behavior** | `PerangkatIoT.java`, `SensorSuhu.java`, `KipasPintar.java` | Memiliki atribut variabel (`idPerangkat`, `suhuTerakhir`), method void prosedur (`tampilkanInfo()`, `proses()`), dan method fungsi return value (`getSuhuTerakhir()`, `isPanas()`). |
| **2** | **Immutability** | `SensorSuhu.java`, `PerangkatIoT.java` | • Konstanta global: `public static final double BATAS_SUHU_PANAS = 30.0;`<br>• Atribut kekal instans: `private final String idPerangkat;` (tidak ada setter, nilai permanen). |
| **3** | **Enkapsulasi & Overloading** | Seluruh Class | • Seluruh atribut bertipe `private`.<br>• Getter dan Setter disediakan lengkap.<br>• Keyword `this` digunakan untuk merujuk atribut instans.<br>• Masing-masing class memiliki **minimal dua variasi constructor** (Constructor Overloading). |
| **4** | **Inheritance** | `SensorSuhu.java`, `KipasPintar.java` | Menggunakan keyword `extends PerangkatIoT`. Class `Main.java` dipisahkan secara independen. |
| **5** | **Super Chaining** | `SensorSuhu.java`, `KipasPintar.java` | • Constructor memanggil `super(idPerangkat, namaPerangkat)` atau `super(..., true)`.<br>• Method `tampilkanInfo()` memanggil `super.tampilkanInfo();`. |
| **6** | **Method Overriding** | `SensorSuhu.java`, `KipasPintar.java` | Menggunakan anotasi `@Override` pada method `tampilkanInfo()`, `proses(double suhu)`, `aktifkan()`, `nonaktifkan()`, dan `tampilkanStatus()`. |
| **7** | **Abstract Class** | `PerangkatIoT.java` | Ditandai `public abstract class PerangkatIoT` dengan abstract method `public abstract void proses(double suhu);`. |
| **8** | **Logika State & Percabangan** | `SensorSuhu.java` & `KipasPintar.java` | Implementasi `proses(double suhu)` menggunakan percabangan `if (suhu >= BATAS_SUHU_PANAS) ... else ...` yang mengubah state internal `kondisiSuhu`, `statusAktif`, dan `levelKecepatan`. |
| **9** | **Polimorfisme Interface** | `Main.java` (baris 30-33) | Pemanggilan method melalui variabel referensi bertipe interface:<br>`BisaDimonitor monitorKipas = kipas;`<br>`BisaDikendalikan kontrolKipas = kipas;`<br>`kontrolKipas.aktifkan();` |
| **10** | **Multiple Interface** | `KipasPintar.java` | Mengimplementasikan dua interface sekaligus:<br>`public class KipasPintar extends PerangkatIoT implements BisaDimonitor, BisaDikendalikan` |

---

## 7. Petunjuk Kompilasi Melalui Terminal

Buka terminal (Command Prompt, PowerShell, atau Terminal di VS Code / IntelliJ IDEA) pada direktori proyek:

```bash
cd c:\xampp\htdocs\IoT_Smart_Monitoring
```

Lakukan kompilasi seluruh file Java sekaligus:
```bash
javac *.java
```

> **Catatan**: Jika perintah `javac` tidak dikenali di terminal Anda, pastikan path JDK sudah terdaftar di PATH environment variable (misal OpenJDK 17/21/26).

---

## 8. Petunjuk Menjalankan Program

### Menjalankan Aplikasi Utama (Interaktif)
Jalankan perintah berikut:
```bash
java Main
```
*Atau di Windows cukup klik dua kali (double click) file [`run.bat`](file:///c:/xampp/htdocs/IoT_Smart_Monitoring/run.bat).*

### Menjalankan Program Verifikasi 10 Requirement (Automated Test)
Untuk membuktikan bahwa seluruh 10 requirement PBO telah lulus verifikasi kode:
```bash
java TestOOP
```
*Atau di Windows cukup klik dua kali file [`test.bat`](file:///c:/xampp/htdocs/IoT_Smart_Monitoring/test.bat).*

---

## 9. Instruksi Screenshot Hasil Program

Simpan file tangkapan layar (screenshot) pengujian program ke dalam direktori `screenshots/`:

1. `screenshots/01_input_panas_32C.png`  
   Tangkapan layar Menu 1 saat input suhu 32°C (suhu panas, kipas otomatis ON kecepatan 2).
2. `screenshots/02_input_normal_24C.png`  
   Tangkapan layar Menu 1 saat input suhu 24°C (suhu normal, kipas otomatis OFF).
3. `screenshots/03_kontrol_manual_aktif.png`  
   Tangkapan layar Menu 3 saat mengaktifkan kipas secara manual.
4. `screenshots/04_kontrol_manual_nonaktif.png`  
   Tangkapan layar Menu 4 saat mematikan kipas secara manual.
5. `screenshots/05_status_ruangan.png`  
   Tangkapan layar Menu 5 menampilkan status monitoring terkini.
6. `screenshots/06_informasi_perangkat.png`  
   Tangkapan layar Menu 2 menampilkan informasi perangkat (Overriding & Super Chaining).
7. `screenshots/07_validasi_input.png`  
   Tangkapan layar penanganan error ketika pengguna memasukkan teks bukan angka.
8. `screenshots/08_verifikasi_test_oop.png`  
   Tangkapan layar hasil eksekusi `java TestOOP` yang menunjukkan 10/10 requirement terpenuhi.
