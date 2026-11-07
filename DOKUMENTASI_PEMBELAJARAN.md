# DOKUMENTASI PEMBELAJARAN & PANDUAN DEFENSE TES LISAN
## SMART ROOM MONITORING SYSTEM
### Tugas Responsi Akhir Praktikum Pemrograman Berorientasi Objek (PBO)
**Jurusan Informatika - Fakultas Teknik - Universitas Jenderal Soedirman**

---

## DAFTAR ISI
1. [Bedah Arsitektur & Hubungan Antarclass](#1-bedah-arsitektur--hubungan-antarclass)
2. [Penjelasan Rinci Seluruh Class, Atribut, Constructor, dan Method](#2-penjelasan-rinci-komponen-kode)
   - 2.1. `PerangkatIoT.java`
   - 2.2. `SensorSuhu.java`
   - 2.3. `KipasPintar.java`
   - 2.4. `BisaDimonitor.java`
   - 2.5. `BisaDikendalikan.java`
   - 2.6. `Main.java`
3. [Konsep Fundamental OOP & Alasan Penerapannya](#3-konsep-fundamental-oop--alasan-penerapannya)
   - Alasan Menggunakan Abstract Class
   - Alasan Menggunakan Inheritance
   - Perbedaan Constructor Overloading vs Method Overriding
   - Fungsi dan Peran Keyword `this` dan `super`
   - Cara Kerja Interface dan Multiple Interface
   - Cara Kerja Polimorfisme Interface
   - Mekanisme Perubahan State Objek Saat Suhu Berubah
4. [Pembuktian Lengkap 10 Requirement PBO pada Source Code](#4-pembuktian-lengkap-10-requirement-pbo)
5. [Bank Pertanyaan Tes Lisan / Defense Responsi (25 Q&A Lengkap)](#5-bank-pertanyaan-tes-lisan--defense-responsi)

---

## 1. BEDAH ARSITEKTUR & HUBUNGAN ANTARCLASS

Diagram hubungan antarclass dapat digambarkan sebagai berikut:

```text
                  +--------------------------------+
                  |     <<Interface>>              |
                  |     BisaDimonitor              |
                  +--------------------------------+
                  | + tampilkanStatus(): void      |
                  +--------------------------------+
                                   ^
                                   | (implements)
  +--------------------------+     |     +------------------------------+
  |    <<Abstract Class>>    |     |     |        <<Interface>>         |
  |       PerangkatIoT       |     |     |       BisaDikendalikan       |
  +--------------------------+     |     +------------------------------+
  | - idPerangkat: String    |     |     | + aktifkan(): void           |
  | - namaPerangkat: String  |     |     | + nonaktifkan(): void        |
  | - statusAktif: boolean   |     |     +------------------------------+
  +--------------------------+     |                    ^
  | + tampilkanInfo(): void  |     |                    | (implements)
  | + proses(suhu): void*    |     |                    |
  +--------------------------+     |                    |
        ^              ^           |                    |
        |              |           +---------+----------+
(extends)              (extends)             |
        |                                    |
+--------------------------+      +------------------------------+
|        SensorSuhu        |      |          KipasPintar         |
+--------------------------+      +------------------------------+
| + BATAS_SUHU_PANAS: dbl  |      | - levelKecepatan: int        |
| - satuan: String         |      | - mode: String               |
| - suhuTerakhir: double   |      +------------------------------+
| - kondisiSuhu: String    |      | + proses(suhu): void         |
+--------------------------+      | + tampilkanStatus(): void    |
| + proses(suhu): void     |      | + aktifkan(): void           |
| + tampilkanInfo(): void  |      | + nonaktifkan(): void        |
| + isPanas(): boolean     |      | + tampilkanInfo(): void      |
+--------------------------+      +------------------------------+
```

### Relasi dalam Sistem:
1. **Inheritance (`extends`)**: `SensorSuhu` dan `KipasPintar` adalah anak kelas (*subclass*) dari `PerangkatIoT`. Keduanya mewarisi identitas perangkat (ID, nama, status aktif) dan method dasar `tampilkanInfo()`.
2. **Realization / Implementation (`implements`)**: `KipasPintar` merealisasikan dua kontrak perilaku sekaligus, yaitu `BisaDimonitor` dan `BisaDikendalikan`.
3. **Association & Dependency**: `Main.java` bertindak sebagai controller yang menginstansiasi objek sensor dan kipas, mengalirkan data masukan suhu, serta mengontrol kipas melalui variabel referensi interface.

---

## 2. PENJELASAN RINCI KOMPONEN KODE

### 2.1. `PerangkatIoT.java` (Abstract Superclass)
- **Fungsi Class**: Sebagai cetak biru (*blueprint*) dasar untuk seluruh jenis perangkat IoT dalam ekosistem monitoring ruangan. Menampung data umum yang dimiliki oleh setiap perangkat keras IoT.
- **Atribut**:
  - `private final String idPerangkat`: Menyimpan nomor identitas unik perangkat (misal `"SNS-001"`). Bersifat `final` sehingga setelah objek dibuat, nilainya **tidak dapat diubah** (Immutability).
  - `private String namaPerangkat`: Menyimpan nama deskriptif perangkat (misal `"Sensor Suhu Ruangan Utama"`).
  - `private boolean statusAktif`: Menyimpan status daya perangkat (`true` jika menyala/aktif, `false` jika mati).
- **Constructor**:
  - `PerangkatIoT(String idPerangkat, String namaPerangkat)`: Constructor dengan 2 parameter. Mengisi ID dan Nama, dengan status daya awal otomatis `false` (standby).
  - `PerangkatIoT(String idPerangkat, String namaPerangkat, boolean statusAktif)`: Constructor dengan 3 parameter. Digunakan saat perangkat ingin langsung ditentukan status daya awalnya saat instansiasi.
- **Method**:
  - `getIdPerangkat()`: Mengembalikan ID perangkat. **Catatan**: Tidak disediakan setter untuk `idPerangkat` demi menjamin sifat *Immutability*.
  - `getNamaPerangkat()` & `setNamaPerangkat(String)`: Mengakses dan memperbarui nama perangkat.
  - `isStatusAktif()` & `setStatusAktif(boolean)`: Mengakses dan memperbarui status daya perangkat.
  - `tampilkanInfo()`: Method prosedur (`void`) untuk mencetak informasi umum perangkat (ID, Nama, Status Daya).
  - `public abstract void proses(double suhu)`: *Abstract method* tanpa body code (`{}`). Mengharuskan setiap subclass membuat implementasi logika pemrosesan suhunya masing-masing.

---

### 2.2. `SensorSuhu.java` (Subclass PerangkatIoT)
- **Fungsi Class**: Merepresentasikan sensor IoT yang bertugas membaca suhu ruangan, menyimpan riwayat suhu terakhir, dan mengklasifikasikan apakah kondisi ruangan dalam kategori "PANAS" atau "NORMAL".
- **Konstanta Global**:
  - `public static final double BATAS_SUHU_PANAS = 30.0`: Ambang batas temperatur panas ruangan. Bersifat `public` agar dapat diakses dari mana saja, `static` karena nilainya sama untuk semua objek sensor, dan `final` karena nilainya konstan tidak boleh diubah.
- **Atribut**:
  - `private String satuan`: Satuan suhu, default `"°C"`.
  - `private double suhuTerakhir`: Nilai suhu ruangan yang terakhir kali dibaca oleh sensor.
  - `private String kondisiSuhu`: Kategori suhu saat ini (`"PANAS"`, `"NORMAL"`, atau `"BELUM DIUKUR"`).
- **Constructor**:
  - `SensorSuhu(String idPerangkat, String namaPerangkat)`: Memanggil constructor superclass menggunakan `super(idPerangkat, namaPerangkat, true)`. Satuan disetel default `"°C"`.
  - `SensorSuhu(String idPerangkat, String namaPerangkat, String satuan)`: Memungkinkan penentuan satuan suhu secara kustom saat inisialisasi.
- **Method**:
  - `getSatuan()`, `setSatuan()`, `getSuhuTerakhir()`, `setSuhuTerakhir()`, `getKondisiSuhu()`, `setKondisiSuhu()`: Getter dan setter standar enkapsulasi.
  - `isPanas()`: Method fungsi yang mengembalikan nilai boolean (`true` jika `suhuTerakhir >= BATAS_SUHU_PANAS`).
  - `@Override public void proses(double suhu)`: Mengimplementasikan abstract method dari superclass. Memperbarui `suhuTerakhir`, lalu menggunakan struktur percabangan `if (suhu >= BATAS_SUHU_PANAS)` untuk mengubah `kondisiSuhu` menjadi `"PANAS"` atau `"NORMAL"`.
  - `@Override public void tampilkanInfo()`: Meng-override method superclass. Memanggil `super.tampilkanInfo()` untuk mencetak info dasar, lalu mencetak rincian spesifik sensor (suhu terakhir, satuan, kondisi, batas panas).

---

### 2.3. `KipasPintar.java` (Subclass & Multiple Interface)
- **Fungsi Class**: Merepresentasikan aktuator pendingin ruangan pintar. Mampu merespon suhu secara otomatis (menyala saat panas, mati saat normal) serta menerima instruksi manual dari pengguna.
- **Atribut**:
  - `private int levelKecepatan`: Kecepatan kipas (0 = mati, 1 = rendah, 2 = sedang, 3 = tinggi).
  - `private String mode`: Mode operasi kipas (`"OTOMATIS"` atau `"MANUAL"`).
- **Constructor**:
  - `KipasPintar(String idPerangkat, String namaPerangkat)`: Constructor standar. Memanggil `super(idPerangkat, namaPerangkat)`, kecepatan awal 0, mode `"OTOMATIS"`.
  - `KipasPintar(String idPerangkat, String namaPerangkat, int levelKecepatan, String mode)`: Constructor spesifik untuk menentukan level kecepatan dan mode awal.
- **Method**:
  - `getLevelKecepatan()`, `setLevelKecepatan(int)`: Getter dan setter kecepatan. Jika level > 0, status aktif otomatis menjadi `true`.
  - `getMode()`, `setMode(String)`: Getter dan setter mode operasi.
  - `getKeteranganKecepatan()`: Method fungsi pengembali nilai String deskripsi kecepatan (misal `"2 (Sedang)"` atau `"0 (Mati / Standby)"`).
  - `@Override public void proses(double suhu)`: Implementasi abstract method superclass. Menyetel mode ke `"OTOMATIS"`. Jika `suhu >= SensorSuhu.BATAS_SUHU_PANAS`, kipas diaktifkan (`statusAktif = true`, `levelKecepatan = 2`). Jika sebaliknya, kipas dimatikan (`statusAktif = false`, `levelKecepatan = 0`).
  - `@Override public void aktifkan()`: Implementasi interface `BisaDikendalikan`. Mengaktifkan kipas secara manual (`levelKecepatan = 2`, `mode = "MANUAL"`).
  - `@Override public void nonaktifkan()`: Implementasi interface `BisaDikendalikan`. Mematikan kipas secara manual (`levelKecepatan = 0`, `mode = "MANUAL"`).
  - `@Override public void tampilkanStatus()`: Implementasi interface `BisaDimonitor`. Mencetak status daya, mode operasi, dan kecepatan kipas.
  - `@Override public void tampilkanInfo()`: Meng-override method superclass dengan bantuan `super.tampilkanInfo()`.

---

### 2.4. `BisaDimonitor.java` (Interface)
- **Fungsi Interface**: Sebagai kontrak perilaku (*behavior contract*) yang menjamin bahwa setiap objek yang mengimplementasikannya pasti memiliki kemampuan untuk menampilkan status pemantauannya melalui method `tampilkanStatus()`.

---

### 2.5. `BisaDikendalikan.java` (Interface)
- **Fungsi Interface**: Sebagai kontrak perilaku untuk perangkat aktuator yang dapat menerima perintah kontrol daya, yaitu `aktifkan()` dan `nonaktifkan()`.

---

### 2.6. `Main.java` (Entry Point & UI Controller)
- **Fungsi Class**: Entry point eksekusi sistem. Mengelola perulangan menu utama, membaca input pengguna melalui `Scanner`, menangani validasi kesalahan data (anti-crash), dan menerapkan polimorfisme interface.

---

## 3. KONSEP FUNDAMENTAL OOP & ALASAN PENERAPANNYA

### 1. Alasan Menggunakan Abstract Class
- `PerangkatIoT` dijadikan **abstract class** karena "perangkat IoT" adalah sebuah konsep generik. Di dunia nyata, tidak ada wujud fisik perangkat yang hanya bernama "perangkat IoT"; yang ada adalah wujud konkretnya, seperti *sensor suhu*, *kipas angin*, *lampu*, atau *kamera*.
- Mencegah instansiasi objek mentah: Menggunakan `new PerangkatIoT(...)` dilarang oleh compiler.
- Mewariskan kesamaan (*common code reuse*): Seluruh perangkat pasti punya ID, nama, dan status daya, sehingga kode tersebut cukup ditulis sekali di superclass.
- Menegakkan standarisasi (*enforcing method*): Melalui `public abstract void proses(double suhu)`, abstract class memaksa setiap turunan wajib membuat cara memproses suhu sesuai karakteristik masing-masing.

### 2. Alasan Menggunakan Inheritance
- **Reusabilitas Kode (*Code Reuse*)**: Menghindari duplikasi penulisan atribut ID, nama, status daya, getter, dan setter di setiap perangkat baru.
- **Struktur Hierarki Logis**: Membangun hubungan *is-a* (SensorSuhu *is a* PerangkatIoT, KipasPintar *is a* PerangkatIoT).
- **Kemudahan Perluasan (*Extensibility*)**: Jika di masa depan ingin menambahkan perangkat baru (misal `SensorKelembaban` atau `LampuPintar`), kita cukup mengekstensikan `PerangkatIoT` tanpa mengubah kode yang sudah ada.

### 3. Perbedaan Constructor Overloading vs Method Overriding
| Aspek | Overloading (Constructor / Method) | Overriding (Method) |
|---|---|---|
| **Definisi** | Membuat beberapa method/constructor dengan **nama yang sama** di dalam **class yang sama**, tetapi memiliki **parameter yang berbeda** (jumlah atau tipe data). | Menuliskan kembali method superclass di dalam **subclass** dengan **nama, tipe return, dan parameter yang identik**, untuk memberikan implementasi yang lebih spesifik. |
| **Lokasi** | Terjadi di dalam class yang sama. | Terjadi antara class induk (superclass) dan anak (subclass). |
| **Anotasi** | Tidak menggunakan anotasi. | Menggunakan anotasi `@Override`. |
| **Waktu Ikat** | *Compile-time polymorphism* (Static binding). | *Run-time polymorphism* (Dynamic binding). |
| **Contoh di Project** | `SensorSuhu(id, nama)` dan `SensorSuhu(id, nama, satuan)` | `SensorSuhu.tampilkanInfo()` meng-override `PerangkatIoT.tampilkanInfo()`. |

### 4. Fungsi Keyword `this` dan `super`
- **Keyword `this`**:
  - Merujuk pada objek instans saat ini (*current instance*).
  - Digunakan untuk membedakan antara variabel instans (atribut class) dengan parameter method/constructor saat namanya sama (`this.namaPerangkat = namaPerangkat;`).
  - Digunakan untuk memanggil method di dalam objek yang sama (`this.levelKecepatan = 2;`).
- **Keyword `super`**:
  - Merujuk pada objek superclass langsung (*immediate parent class*).
  - `super(...)`: Digunakan pada baris pertama constructor subclass untuk memanggil constructor superclass (*Super Chaining*).
  - `super.namaMethod()`: Digunakan untuk mengeksekusi logika asli milik superclass sebelum subclass menambahkan informasi tambahannya (misal `super.tampilkanInfo()`).

### 5. Cara Kerja Interface dan Multiple Interface
- **Interface** adalah kontrak perilaku (*pure abstraction*) yang mendefinisikan apa yang harus dilakukan suatu class tanpa menentukan bagaimana caranya.
- Java tidak mendukung *multiple inheritance* untuk class (sebuah class tidak boleh `extends` lebih dari satu class karena masalah ambiguitas *Diamond Problem*).
- Namun, Java mengizinkan **Multiple Interface**: Sebuah class dapat mengimplementasikan banyak interface sekaligus menggunakan keyword `implements InterfaceA, InterfaceB`.
- Pada project ini, `KipasPintar` mengimplementasikan `BisaDimonitor` (dapat dipantau statusnya) dan `BisaDikendalikan` (dapat dinyalakan/dimatikan). Hal ini sangat logis karena kipas merupakan aktuator yang bisa diatur sekaligus dimonitor.

### 6. Cara Kerja Polimorfisme Interface
Polimorfisme memungkinkan suatu objek memiliki banyak bentuk. Pada project ini:
```java
BisaDimonitor monitorKipas = kipas;
BisaDikendalikan kontrolKipas = kipas;
```
- Objek aktual di memori adalah satu objek `KipasPintar`.
- Ketika direferensikan sebagai `BisaDimonitor`, kita membatasi hak akses pemanggil hanya pada method-method yang ada di kontrak monitoring (`tampilkanStatus()`).
- Ketika direferensikan sebagai `BisaDikendalikan`, pemanggil hanya diberikan hak akses ke fungsi kontrol (`aktifkan()` dan `nonaktifkan()`).
- Ini menerapkan prinsip *Interface Segregation* (pemisahan tanggung jawab antarmuka) yang membuat sistem sangat modular dan aman.

### 7. Mekanisme Perubahan State Objek Saat Suhu Berubah
Ketika pengguna menginputkan nilai suhu melalui Menu 1:
1. `sensor.proses(suhu)` dieksekusi:
   - Atribut `suhuTerakhir` diperbarui dengan nilai input.
   - Evaluasi percabangan: Jika `suhu >= 30.0`, atribut `kondisiSuhu` berubah menjadi `"PANAS"`. Jika `< 30.0`, menjadi `"NORMAL"`.
2. `kipas.proses(suhu)` dieksekusi:
   - Atribut `mode` disetel menjadi `"OTOMATIS"`.
   - Evaluasi percabangan: Jika `suhu >= 30.0`, method memanggil `setStatusAktif(true)` dan menyetel `levelKecepatan = 2` (kipas berputar). Jika `< 30.0`, memanggil `setStatusAktif(false)` dan menyetel `levelKecepatan = 0` (kipas mati).
3. Saat pengguna memilih menu kontrol manual (Menu 3 atau 4):
   - Atribut `mode` berubah menjadi `"MANUAL"`.
   - `levelKecepatan` dan `statusAktif` diperbarui langsung sesuai perintah pengguna tanpa terikat suhu saat itu.

---

## 4. PEMBUKTIAN LENGKAP 10 REQUIREMENT PBO

Berikut rujukan baris kode untuk menjawab jika dosen menanyakan bukti requirement:

1. **Req 1: State dan Behavior**
   - Bukti State: Atribut `private double suhuTerakhir;` di `SensorSuhu.java` baris 22.
   - Bukti Method Void: `public void proses(double suhu)` di `SensorSuhu.java` baris 81.
   - Bukti Method Return Value: `public boolean isPanas()` di `SensorSuhu.java` baris 68 mengembalikan boolean, dan `getIdPerangkat()` di `PerangkatIoT.java` baris 48 mengembalikan String.
2. **Req 2: Immutability**
   - Bukti Global Constant: `public static final double BATAS_SUHU_PANAS = 30.0;` di `SensorSuhu.java` baris 18.
   - Bukti Immutable Attribute: `private final String idPerangkat;` di `PerangkatIoT.java` baris 17 (tanpa method setter).
3. **Req 3: Enkapsulasi dan Overloading**
   - Bukti Enkapsulasi: Seluruh atribut di semua class bermodifier `private` dan diakses melalui getter/setter. Penggunaan `this` ada di setiap constructor/setter.
   - Bukti Overloading: `SensorSuhu(String, String)` dan `SensorSuhu(String, String, String)` di `SensorSuhu.java` baris 31-48.
4. **Req 4: Inheritance**
   - Bukti: `public class SensorSuhu extends PerangkatIoT` di `SensorSuhu.java` baris 15, dan `Main.java` berdiri terpisah.
5. **Req 5: Super Chaining**
   - Bukti Constructor: `super(idPerangkat, namaPerangkat, true);` di `SensorSuhu.java` baris 32.
   - Bukti Method: `super.tampilkanInfo();` di `SensorSuhu.java` baris 98.
6. **Req 6: Method Overriding**
   - Bukti: Anotasi `@Override` pada `public void tampilkanInfo()` di `SensorSuhu.java` baris 97 dan `KipasPintar.java` baris 131.
7. **Req 7: Abstract Class**
   - Bukti: Deklarasi `public abstract class PerangkatIoT` di `PerangkatIoT.java` baris 14 dan abstract method `public abstract void proses(double suhu);` di baris 75.
8. **Req 8: Logika State dan Percabangan**
   - Bukti: Struktur `if (suhu >= BATAS_SUHU_PANAS) { this.kondisiSuhu = "PANAS"; } else { ... }` di `SensorSuhu.java` baris 85-89 yang langsung mengubah atribut objek.
9. **Req 9: Polimorfisme Interface**
   - Bukti: Di `Main.java` baris 30-33:
     ```java
     BisaDimonitor monitorKipas = kipas;
     BisaDikendalikan kontrolKipas = kipas;
     kontrolKipas.aktifkan();
     ```
10. **Req 10: Multiple Interface**
    - Bukti: `public class KipasPintar extends PerangkatIoT implements BisaDimonitor, BisaDikendalikan` di `KipasPintar.java` baris 16.

---

## 5. BANK PERTANYAAN TES LISAN / DEFENSE RESPONSI (25 Q&A LENGKAP)

Berikut 25 pertanyaan yang sangat sering diajukan dosen penguji saat responsi akhir PBO beserta jawaban terbaik yang mengacu pada kode program:

#### Q1: Jelaskan apa tujuan dari pembuatan program Smart Room Monitoring System ini!
> **Jawaban**: Program ini merupakan simulasi sistem IoT berbasis Java untuk memantau suhu ruangan dan mengatur aktuator kipas secara otomatis maupun manual. Ketika suhu terbaca mencapai atau melebihi 30.0°C, sistem mendeteksi kondisi panas dan secara otomatis mengaktifkan kipas pada kecepatan sedang. Jika suhu di bawah 30.0°C, kipas otomatis dimatikan. Selain otomatis, pengguna juga dapat mengendalikan kipas secara manual melalui antarmuka terminal.

#### Q2: Mengapa class `PerangkatIoT` dibuat sebagai abstract class dan bukan class biasa?
> **Jawaban**: Karena `PerangkatIoT` adalah entitas konseptual umum yang merepresentasikan semua perangkat IoT, bukan perangkat fisik yang nyata. Kita tidak boleh membuat objek langsung dari `PerangkatIoT` (`new PerangkatIoT()`), melainkan harus menginstansiasi turunan konkretnya seperti `SensorSuhu` atau `KipasPintar`. Selain itu, `PerangkatIoT` memiliki abstract method `proses(double suhu)` yang wajib diimplementasikan secara berbeda oleh masing-masing subclass.

#### Q3: Mengapa atribut `idPerangkat` menggunakan modifier `final` dan tidak dibuatkan setter?
> **Jawaban**: Hal ini untuk menerapkan prinsip **Immutability** pada level instans objek. Identitas perangkat keras (ID Perangkat) bersifat permanen sejak perangkat diproduksi/didaftarkan ke sistem. Dengan menjadikannya `private final` dan tidak menyediakan setter, nilai ID perangkat dijamin tidak dapat diubah secara sengaja maupun tidak sengaja setelah proses inisialisasi pada constructor selesai.

#### Q4: Apa perbedaan antara `public static final` pada `SensorSuhu` dan `private final` pada `PerangkatIoT`?
> **Jawaban**: `public static final double BATAS_SUHU_PANAS` adalah **konstanta global** di tingkat class. Kata kunci `static` membuat variabel tersebut hanya disimpan satu kali di memori dan dimiliki bersama oleh semua objek tanpa perlu instansiasi. Sedangkan `private final String idPerangkat` adalah **atribut instans kekal**, artinya setiap objek memiliki nilainya masing-masing, nilainya ditentukan saat objek dibuat melalui constructor, dan tidak dapat diubah lagi untuk objek tersebut.

#### Q5: Tunjukkan di mana letak Super Chaining pada program dan jelaskan tujuannya!
> **Jawaban**: Super chaining terjadi pada dua tempat:
> 1. Pada constructor `SensorSuhu`, perintah `super(idPerangkat, namaPerangkat, true)` dipanggil untuk mendelegasikan inisialisasi atribut `idPerangkat`, `namaPerangkat`, dan `statusAktif` ke constructor class induk (`PerangkatIoT`).
> 2. Pada method `tampilkanInfo()`, perintah `super.tampilkanInfo()` dipanggil untuk mencetak data umum terlebih dahulu, lalu subclass menambahkan data spesifiknya sendiri. Tujuannya adalah mencegah penulisan kode berulang (*DRY - Don't Repeat Yourself*).

#### Q6: Mengapa method `tampilkanInfo()` di `SensorSuhu` memerlukan anotasi `@Override`?
> **Jawaban**: Anotasi `@Override` menandakan bahwa subclass mengganti atau memperluas implementasi method `tampilkanInfo()` yang sudah ada di superclass `PerangkatIoT`. Penggunaan `@Override` juga membantu compiler memeriksa apakah signature (nama, parameter, return type) method tersebut benar-benar cocok dengan method di superclass. Jika kita salah mengetik nama method, compiler akan langsung memunculkan error sebelum program dijalankan.

#### Q7: Apa yang dimaksud dengan Constructor Overloading dan di mana penerapannya pada kode Anda?
> **Jawaban**: Constructor Overloading adalah penyediaan lebih dari satu constructor dalam satu class dengan nama yang sama persis namun memiliki daftar parameter yang berbeda. Di class `KipasPintar`, terdapat dua constructor:
> 1. `KipasPintar(String id, String nama)`: inisialisasi standar dengan kipas mati dan kecepatan 0.
> 2. `KipasPintar(String id, String nama, int levelKecepatan, String mode)`: inisialisasi dengan kecepatan dan mode awal yang ditentukan pengguna.

#### Q8: Apa bedanya Overloading dan Overriding?
> **Jawaban**: 
> - **Overloading**: Terjadi dalam **satu class yang sama**, memiliki nama method/constructor yang sama tetapi signature parameternya berbeda. Dikenal sebagai *compile-time polymorphism*.
> - **Overriding**: Terjadi antara **superclass dan subclass**, di mana subclass mendefinisikan ulang method superclass dengan nama dan parameter yang persis sama. Dikenal sebagai *runtime polymorphism*.

#### Q9: Mengapa class `KipasPintar` mengimplementasikan dua interface (`BisaDimonitor` dan `BisaDikendalikan`)?
> **Jawaban**: Karena dalam dunia nyata, kipas pintar memiliki dua peran fungsi: dapat **dipantau kondisinya** (apakah sedang menyala, mode apa, dan berapa kecepatannya) melalui interface `BisaDimonitor`, sekaligus dapat **dikendalikan operasinya** (dinyalakan atau dimatikan) melalui interface `BisaDikendalikan`. Ini merupakan penerapan konsep **Multiple Interface**.

#### Q10: Jelaskan bagaimana konsep Polimorfisme Interface diimplementasikan di `Main.java`!
> **Jawaban**: Di `Main.java`, objek `kipas` (bertipe `KipasPintar`) ditugaskan ke dalam variabel referensi bertipe interface:
> ```java
> BisaDimonitor monitorKipas = kipas;
> BisaDikendalikan kontrolKipas = kipas;
> ```
> Saat menu kontrol manual dipilih, program mengeksekusi `kontrolKipas.aktifkan()`. Pemanggil hanya memandang objek tersebut sebagai instans dari `BisaDikendalikan`, menyembunyikan detail implementasi internal dari kipas pintar. Ini adalah bukti nyata Polimorfisme Interface.

#### Q11: Apa keuntungan memisahkan antarmuka menjadi dua interface (`BisaDimonitor` dan `BisaDikendalikan`) dibanding menjadikannya satu interface saja?
> **Jawaban**: Hal ini menerapkan prinsip *Interface Segregation Principle* (prinsip 'I' dalam SOLID). Jika kita menggabungkannya menjadi satu, perangkat yang hanya bisa dipantau tetapi tidak bisa dikendalikan (misalnya sensor pasif) akan dipaksa mengimplementasikan method `aktifkan()` dan `nonaktifkan()`, yang mana tidak masuk akal bagi sensor. Dengan memisahkannya, sistem menjadi lebih fleksibel dan modular.

#### Q12: Bagaimana alur perubahan state pada kipas ketika pengguna menginputkan suhu 35.0°C?
> **Jawaban**:
> 1. Di Menu 1, nilai `35.0` dikirim ke `sensor.proses(35.0)`.
> 2. Sensor mengevaluasi `35.0 >= 30.0` (true), sehingga `kondisiSuhu` sensor berubah menjadi `"PANAS"`.
> 3. Nilai `35.0` juga dikirim ke `kipas.proses(35.0)`.
> 4. Kipas mengevaluasi `35.0 >= 30.0` (true), sehingga mode diubah menjadi `"OTOMATIS"`, status aktif diubah menjadi `true`, dan `levelKecepatan` diubah dari `0` menjadi `2` (sedang).

#### Q13: Apa yang terjadi jika pengguna menyalakan kipas secara manual melalui Menu 3, lalu membuka Menu 5 (Status)?
> **Jawaban**: Saat Menu 3 dipilih, method `kontrolKipas.aktifkan()` dieksekusi. State kipas berubah: `statusAktif` menjadi `true`, `levelKecepatan` menjadi `2`, dan `mode` menjadi `"MANUAL"`. Ketika Menu 5 dibuka, method `monitorKipas.tampilkanStatus()` dipanggil dan secara konsisten menampilkan bahwa kipas sedang MENYALA dalam mode MANUAL.

#### Q14: Bagaimana sistem Anda menangani input suhu yang tidak valid, misalnya pengguna memasukkan huruf "abc"?
> **Jawaban**: Pada `Main.java`, input dibaca di dalam blok `try-catch` yang menangkap exception `InputMismatchException`. Jika pengguna memasukkan huruf atau karakter non-angka, program tidak akan mengalami crash (berhenti tiba-tiba), melainkan menampilkan pesan error yang ramah pengguna, mengeksekusi `scanner.nextLine()` untuk membersihkan buffer, dan kembali ke menu dengan aman.

#### Q15: Apa fungsi keyword `this` pada method `setNamaPerangkat(String namaPerangkat)`?
> **Jawaban**: Keyword `this` merujuk pada objek instans saat ini. Pada baris `this.namaPerangkat = namaPerangkat;`, terdapat ambiguitas karena nama variabel instans dan nama parameter method persis sama. `this.namaPerangkat` menunjuk ke atribut class, sedangkan `namaPerangkat` tanpa `this` menunjuk ke parameter yang dilewatkan ke dalam method.

#### Q16: Bisakah kita menginstansiasi interface secara langsung, misalnya `BisaDikendalikan k = new BisaDikendalikan();`? Mengapa?
> **Jawaban**: Tidak bisa. Interface hanya berupa deklarasi kontrak tanpa implementasi method konkrit. Compiler Java akan menolak pembuatan objek langsung dari interface karena tidak ada body kode yang bisa dieksekusi di memori. Interface hanya dapat diinstansiasi melalui class konkret yang mengimplementasikannya, seperti `BisaDikendalikan k = new KipasPintar("K-1", "Kipas");`.

#### Q17: Mengapa `statusAktif` di `PerangkatIoT` memiliki getter dengan nama `isStatusAktif()` dan bukan `getStatusAktif()`?
> **Jawaban**: Mengikuti standar konvensi penamaan Java (*JavaBeans Naming Conventions*), untuk atribut bertipe data primitif `boolean`, method getter diawali dengan kata `is` (misalnya `isStatusAktif()` atau `isPanas()`), sedangkan untuk tipe data lain menggunakan awalan `get`.

#### Q18: Pada class `SensorSuhu`, mengapa kita tidak membuat variabel baru untuk `idPerangkat` dan `namaPerangkat`?
> **Jawaban**: Karena kedua atribut tersebut sudah dideklarasikan di superclass `PerangkatIoT`. Berkat mekanisme **Inheritance**, `SensorSuhu` secara otomatis mewarisi atribut tersebut. Membuat variabel baru dengan nama yang sama akan menyebabkan *variable shadowing* (penutupan variabel induk), yang melanggar prinsip inheritance dan pemborosan memori.

#### Q19: Apakah sebuah class dapat meng-extends lebih dari satu superclass di Java? Bagaimana solusinya di project ini?
> **Jawaban**: Tidak bisa. Java tidak mendukung *multiple inheritance* untuk class demi menghindari ambiguitas hierarki (*Diamond Problem*). Solusi di Java adalah menggunakan **Interface**. Suatu class hanya boleh meng-`extends` satu class (dalam hal ini `PerangkatIoT`), tetapi dapat meng-`implements` banyak interface sekaligus (dalam hal ini `BisaDimonitor` dan `BisaDikendalikan`).

#### Q20: Bagaimana Anda membuktikan bahwa seluruh 10 requirement PBO sudah benar-benar terpenuhi dan bukan sekadar klaim?
> **Jawaban**: Saya telah membuat class pengujian otomatis bernama `TestOOP.java`. Program tersebut menguji dan memverifikasi ke-10 requirement secara objektif menggunakan assertion kode dan Java Reflection (misal memeriksa modifier abstract, final, dan implementasi interface). Saat `java TestOOP` dijalankan, program mencetak status bahwa ke-10 requirement telah lulus 100%.

#### Q21: Apa perbedaan mendasar antara method prosedur (void) dan method fungsi (return value) pada class Anda?
> **Jawaban**: 
> - **Method Prosedur (`void`)**: Menjalankan instruksi atau mengubah state tanpa menghasilkan nilai balik kepada pemanggil, contohnya `proses(double suhu)` dan `tampilkanInfo()`.
> - **Method Fungsi (`return value`)**: Memproses data dan mengembalikan suatu nilai dengan tipe data tertentu, contohnya `isPanas()` yang mengembalikan nilai bertipe `boolean` dan `getSuhuTerakhir()` yang mengembalikan `double`.

#### Q22: Sebutkan tipe-tipe access modifier di Java dan jelaskan mengapa seluruh atribut di project ini menggunakan `private`!
> **Jawaban**: Access modifier di Java ada 4: `private`, `default` (package-private), `protected`, dan `public`. Seluruh atribut di project ini menggunakan `private` untuk menerapkan prinsip **Enkapsulasi**, yaitu menyembunyikan data internal objek (*data hiding*) agar tidak dapat dimanipulasi atau diakses sembarangan secara langsung dari luar class. Modifikasi hanya diizinkan melalui method getter dan setter yang terkontrol.

#### Q23: Bagaimana cara kerja anotasi `@Override` saat proses kompilasi?
> **Jawaban**: Anotasi `@Override` adalah instruksi metadata untuk compiler. Compiler akan memverifikasi apakah method yang dianotasi benar-benar meng-override method yang ada di class induk atau interface. Jika tidak ditemukan method dengan signature yang identik di superclass (misal ada salah ketik nama atau parameter), compiler langsung mengeluarkan pesan error saat kompilasi.

#### Q24: Apa hubungan antara package `java.util.Scanner` dan `InputMismatchException` pada program `Main.java`?
> **Jawaban**: `Scanner` digunakan untuk membaca token input dari console (`System.in`). Ketika kita memanggil `scanner.nextInt()` atau `scanner.nextDouble()`, `Scanner` mengharapkan tipe data numerik. Jika pengguna memasukkan teks huruf (misal "panas"), `Scanner` akan melempar exception bertipe `InputMismatchException`. Dengan mengimpor dan menangkap exception tersebut menggunakan blok `try-catch`, aplikasi dapat mencegah crash dan meminta input ulang secara aman.

#### Q25: Jika sistem ini ingin diintegrasikan dengan sensor fisik nyata di masa depan (misal ESP32 via MQTT/Serial), bagian mana yang perlu diubah?
> **Jawaban**: Berkat desain OOP yang modular dan terenkapsulasi, kita **tidak perlu merombak seluruh sistem**. Kita hanya perlu membuat satu class baru, misalnya `SensorSuhuHardware extends PerangkatIoT`, atau menambahkan method pembacaan data serial di dalam `SensorSuhu.proses()`. Antarmuka menu dan class `KipasPintar` tidak perlu diubah karena mereka berkomunikasi melalui kontrak method yang sudah terstandarisasi.

