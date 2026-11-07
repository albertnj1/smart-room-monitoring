import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Main Class - Smart Room Monitoring System
 * Entry point aplikasi konsol simulasi IoT Monitoring Ruangan.
 * 
 * Memenuhi seluruh 10 Requirement PBO:
 * 1. State dan Behavior (Atribut, method void, method fungsi return value)
 * 2. Immutability (public static final & private final)
 * 3. Enkapsulasi dan Overloading (private, getter/setter, keyword this, overloaded constructor)
 * 4. Inheritance (Superclass PerangkatIoT, subclass SensorSuhu & KipasPintar, keyword extends)
 * 5. Super Chaining (super(...) pada constructor, super.tampilkanInfo() pada method)
 * 6. Method Overriding (@Override pada tampilkanInfo() dan proses())
 * 7. Abstract Class (public abstract class PerangkatIoT dan abstract method proses())
 * 8. Logika State dan Percabangan (if/else merubah state kondisiSuhu dan status kipas)
 * 9. Polimorfisme Interface (BisaDimonitor dan BisaDikendalikan sebagai tipe referensi)
 * 10. Multiple Interface (KipasPintar implements BisaDimonitor, BisaDikendalikan)
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Instansiasi objek SensorSuhu dan KipasPintar
        // Menggunakan constructor overloading
        SensorSuhu sensor = new SensorSuhu("SNS-001", "Sensor Suhu Ruangan Utama");
        KipasPintar kipas = new KipasPintar("ACT-001", "Kipas Angin Pintar Dinding");

        /*
         * PENERAPAN REQUIREMENT 9: Polimorfisme Interface
         * Objek KipasPintar diakses melalui variabel referensi bertipe Interface:
         * 1. monitor: referensi bertipe BisaDimonitor untuk fungsi pemantauan
         * 2. kontrol: referensi bertipe BisaDikendalikan untuk fungsi kontrol daya
         */
        BisaDimonitor monitorKipas = kipas;
        BisaDikendalikan kontrolKipas = kipas;

        boolean berjalan = true;

        while (berjalan) {
            tampilkanMenuUtama();
            System.out.print("Pilih menu: ");

            int pilihan = -1;
            try {
                pilihan = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("\n[ERROR] Input tidak valid! Masukkan angka menu yang benar.\n");
                scanner.nextLine(); // Membersihkan buffer input
                continue;
            }

            System.out.println(); // Baris baru untuk kerapian tampilan

            switch (pilihan) {
                case 1:
                    // Menu 1: Membaca & memproses suhu ruangan
                    handleInputSuhu(scanner, sensor, kipas);
                    break;

                case 2:
                    // Menu 2: Menampilkan informasi lengkap perangkat (Inheritance & Overriding)
                    handleTampilkanInfoPerangkat(sensor, kipas);
                    break;

                case 3:
                    // Menu 3: Kontrol manual - Aktifkan kipas via Interface BisaDikendalikan
                    System.out.println("========================================");
                    System.out.println("       KONTROL MANUAL: AKTIFKAN KIPAS   ");
                    System.out.println("========================================");
                    kontrolKipas.aktifkan(); // Polimorfisme Interface
                    System.out.println("========================================\n");
                    break;

                case 4:
                    // Menu 4: Kontrol manual - Nonaktifkan kipas via Interface BisaDikendalikan
                    System.out.println("========================================");
                    System.out.println("     KONTROL MANUAL: NONAKTIFKAN KIPAS  ");
                    System.out.println("========================================");
                    kontrolKipas.nonaktifkan(); // Polimorfisme Interface
                    System.out.println("========================================\n");
                    break;

                case 5:
                    // Menu 5: Menampilkan status monitoring ruangan terkini
                    handleTampilkanStatusRuangan(sensor, monitorKipas);
                    break;

                case 0:
                    // Menu 0: Keluar dari program
                    berjalan = false;
                    System.out.println("========================================");
                    System.out.println(" Terima kasih telah menggunakan sistem  ");
                    System.out.println("     SMART ROOM MONITORING SYSTEM!      ");
                    System.out.println("========================================");
                    break;

                default:
                    System.out.println("[PERINGATAN] Menu tidak tersedia. Silakan pilih 0 - 5.\n");
                    break;
            }
        }

        scanner.close();
    }

    /**
     * Menampilkan antarmuka Menu Utama ke konsol.
     */
    private static void tampilkanMenuUtama() {
        System.out.println("========================================");
        System.out.println("      SMART ROOM MONITORING SYSTEM      ");
        System.out.println("========================================");
        System.out.println("1. Masukkan suhu ruangan");
        System.out.println("2. Tampilkan informasi perangkat");
        System.out.println("3. Aktifkan kipas secara manual");
        System.out.println("4. Nonaktifkan kipas secara manual");
        System.out.println("5. Tampilkan status ruangan");
        System.out.println("0. Keluar");
        System.out.println("========================================");
    }

    /**
     * Menangani proses input suhu, kalkulasi kondisi, dan respon otomatis kipas.
     */
    private static void handleInputSuhu(Scanner scanner, SensorSuhu sensor, KipasPintar kipas) {
        System.out.println("========================================");
        System.out.println("          INPUT DATA SUHU RUANGAN       ");
        System.out.println("========================================");
        System.out.print("Masukkan nilai suhu ruangan (" + sensor.getSatuan() + "): ");

        double inputSuhu;
        try {
            inputSuhu = scanner.nextDouble();
        } catch (InputMismatchException e) {
            System.out.println("\n[ERROR] Nilai suhu harus berupa angka desimal atau bulat (contoh: 30.5 atau 28)!");
            scanner.nextLine(); // Bersihkan buffer
            System.out.println("========================================\n");
            return;
        }

        // Validasi batasan suhu wajar ruangan (-10°C s.d. 80°C)
        if (inputSuhu < -10.0 || inputSuhu > 80.0) {
            System.out.println("[PERINGATAN] Suhu di luar rentang wajar ruangan (-10°C s.d. 80°C). Data tetap diproses.");
        }

        // Memproses data suhu pada Sensor dan Kipas Pintar (Polimorfisme PerangkatIoT)
        sensor.proses(inputSuhu);
        kipas.proses(inputSuhu);

        // Menampilkan feedback hasil pengolahan data
        System.out.println("\n--- Hasil Pemantauan Real-Time ---");
        System.out.println("Suhu Terbaca   : " + sensor.getSuhuTerakhir() + " " + sensor.getSatuan());
        System.out.println("Ambang Batas   : " + SensorSuhu.BATAS_SUHU_PANAS + " " + sensor.getSatuan());
        System.out.println("Kondisi Ruangan: " + sensor.getKondisiSuhu());
        
        if (sensor.isPanas()) {
            System.out.println("Respon Sistem  : [PANAS TERDETEKSI] Kipas Otomatis DIAKTIFKAN (Kecepatan: " 
                    + kipas.getKeteranganKecepatan() + ").");
        } else {
            System.out.println("Respon Sistem  : [SUHU NORMAL] Kipas Otomatis DINONAKTIFKAN (Kecepatan: " 
                    + kipas.getKeteranganKecepatan() + ").");
        }
        System.out.println("========================================\n");
    }

    /**
     * Menampilkan informasi seluruh perangkat IoT yang terdaftar dalam sistem.
     * Menguji pemanggilan method overriding dan super chaining.
     */
    private static void handleTampilkanInfoPerangkat(SensorSuhu sensor, KipasPintar kipas) {
        System.out.println("========================================");
        System.out.println("       INFORMASI PERANGKAT IOT          ");
        System.out.println("========================================");
        System.out.println("[ 1. Perangkat Sensor ]");
        sensor.tampilkanInfo();
        System.out.println("----------------------------------------");
        System.out.println("[ 2. Perangkat Aktuator ]");
        kipas.tampilkanInfo();
        System.out.println("========================================\n");
    }

    /**
     * Menampilkan ringkasan status monitoring ruangan saat ini.
     * Menggunakan interface BisaDimonitor untuk menampilkan status kipas.
     */
    private static void handleTampilkanStatusRuangan(SensorSuhu sensor, BisaDimonitor monitorKipas) {
        System.out.println("========================================");
        System.out.println("         STATUS MONITORING RUANGAN      ");
        System.out.println("========================================");
        System.out.println("Suhu Terakhir  : " + sensor.getSuhuTerakhir() + " " + sensor.getSatuan());
        System.out.println("Kondisi Suhu   : " + sensor.getKondisiSuhu());
        System.out.println("Batas Suhu     : " + SensorSuhu.BATAS_SUHU_PANAS + " " + sensor.getSatuan());
        System.out.println("----------------------------------------");
        System.out.println("[ Status Aktuator Pendingin ]");
        // Pemanggilan method melalui variabel bertipe interface (Interface Polymorphism)
        monitorKipas.tampilkanStatus();
        System.out.println("========================================\n");
    }
}

