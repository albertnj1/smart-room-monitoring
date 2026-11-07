/**
 * Subclass KipasPintar
 * Mewarisi sifat dari abstract superclass PerangkatIoT, serta
 * mengimplementasikan dua interface sekaligus (Multiple Interface).
 * Berfungsi sebagai aktuator pendingin ruangan berbasis kendali otomatis maupun manual.
 * 
 * Penerapan Konsep PBO:
 * - Inheritance: Menggunakan keyword 'extends PerangkatIoT'.
 * - Multiple Interface: Menggunakan keyword 'implements BisaDimonitor, BisaDikendalikan'.
 * - Method Overriding: Meng-override proses(), tampilkanInfo(), tampilkanStatus(), aktifkan(), nonaktifkan().
 * - Super Chaining: Menggunakan 'super(...)' pada constructor dan 'super.tampilkanInfo()'.
 * - Logika State & Percabangan: Mengubah status aktif, kecepatan, dan mode kipas berdasarkan suhu.
 * - Constructor Overloading: Memiliki dua variasi constructor.
 */
public class KipasPintar extends PerangkatIoT implements BisaDimonitor, BisaDikendalikan {

    // Atribut privat (Enkapsulasi)
    private int levelKecepatan; // 0 = Mati, 1 = Rendah, 2 = Sedang, 3 = Tinggi
    private String mode;        // "OTOMATIS" atau "MANUAL"

    /**
     * Constructor 1 (Overloading):
     * Menginisialisasi Kipas Pintar dengan pengaturan default (mati, level 0, mode OTOMATIS).
     * Menggunakan Super Chaining ke constructor PerangkatIoT.
     */
    public KipasPintar(String idPerangkat, String namaPerangkat) {
        super(idPerangkat, namaPerangkat); // Super Chaining
        this.levelKecepatan = 0;
        this.mode = "OTOMATIS";
    }

    /**
     * Constructor 2 (Overloading):
     * Menginisialisasi Kipas Pintar dengan kecepatan dan mode awal tertentu.
     */
    public KipasPintar(String idPerangkat, String namaPerangkat, int levelKecepatan, String mode) {
        super(idPerangkat, namaPerangkat, levelKecepatan > 0); // Super Chaining
        this.levelKecepatan = levelKecepatan;
        this.mode = mode;
    }

    // --- Getter dan Setter (Enkapsulasi) ---

    public int getLevelKecepatan() {
        return this.levelKecepatan;
    }

    public void setLevelKecepatan(int levelKecepatan) {
        this.levelKecepatan = levelKecepatan;
        if (levelKecepatan > 0) {
            setStatusAktif(true);
        } else {
            setStatusAktif(false);
        }
    }

    public String getMode() {
        return this.mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * Method Fungsi: Mengembalikan deskripsi tingkat kecepatan kipas.
     * 
     * @return String representasi kecepatan.
     */
    public String getKeteranganKecepatan() {
        switch (this.levelKecepatan) {
            case 1:
                return "1 (Rendah)";
            case 2:
                return "2 (Sedang)";
            case 3:
                return "3 (Tinggi)";
            default:
                return "0 (Mati / Standby)";
        }
    }

    /**
     * Implementasi Abstract Method dari PerangkatIoT.
     * Mengatur state internal kipas secara otomatis berdasarkan suhu.
     * 
     * Aturan Simulasi:
     * - Suhu >= 30.0°C : Kondisi panas -> Kipas otomatis aktif (level 2).
     * - Suhu < 30.0°C  : Kondisi normal -> Kipas otomatis nonaktif (level 0).
     * 
     * @param suhu Nilai suhu ruangan yang dideteksi sistem.
     */
    @Override
    public void proses(double suhu) {
        this.mode = "OTOMATIS";

        // Logika state dan percabangan
        if (suhu >= SensorSuhu.BATAS_SUHU_PANAS) {
            setStatusAktif(true);
            this.levelKecepatan = 2; // Aktif pada kecepatan sedang otomatis
        } else {
            setStatusAktif(false);
            this.levelKecepatan = 0; // Kipas dinonaktifkan
        }
    }

    /**
     * Implementasi Interface BisaDikendalikan.
     * Mengaktifkan kipas secara manual.
     */
    @Override
    public void aktifkan() {
        setStatusAktif(true);
        this.levelKecepatan = 2;
        this.mode = "MANUAL";
        System.out.println("[KONTROL MANUAL] Kipas berhasil DI-AKTIFKAN pada kecepatan " + getKeteranganKecepatan() + ".");
    }

    /**
     * Implementasi Interface BisaDikendalikan.
     * Menonaktifkan kipas secara manual.
     */
    @Override
    public void nonaktifkan() {
        setStatusAktif(false);
        this.levelKecepatan = 0;
        this.mode = "MANUAL";
        System.out.println("[KONTROL MANUAL] Kipas berhasil DI-NONAKTIFKAN (OFF).");
    }

    /**
     * Implementasi Interface BisaDimonitor.
     * Menampilkan status ringkas kipas untuk kebutuhan monitoring.
     */
    @Override
    public void tampilkanStatus() {
        System.out.println("Status Kipas   : " + (isStatusAktif() ? "MENYALA (ON)" : "MATI (OFF)"));
        System.out.println("Mode Operasi   : " + this.mode);
        System.out.println("Kecepatan      : " + getKeteranganKecepatan());
    }

    /**
     * Method Overriding dari PerangkatIoT.
     * Menggunakan super.tampilkanInfo() untuk menampilkan data umum,
     * kemudian menambahkan informasi kecepatan dan mode operasi.
     */
    @Override
    public void tampilkanInfo() {
        super.tampilkanInfo(); // Super Chaining memanggil method superclass
        System.out.println("Tipe Aktuator  : Kipas Ruangan Pintar");
        System.out.println("Mode Operasi   : " + this.mode);
        System.out.println("Kecepatan Saat : " + getKeteranganKecepatan());
    }
}

