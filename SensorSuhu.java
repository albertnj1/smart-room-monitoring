/**
 * Subclass SensorSuhu
 * Mewarisi sifat dari abstract superclass PerangkatIoT.
 * Berfungsi untuk membaca dan memproses data suhu ruangan.
 * 
 * Penerapan Konsep PBO:
 * - Inheritance: Menggunakan keyword 'extends PerangkatIoT'.
 * - Immutability: Memiliki konstanta global 'public static final double BATAS_SUHU_PANAS = 30.0'.
 * - Super Chaining: Memanggil constructor superclass dengan 'super(...)' dan
 *   memanggil method superclass dengan 'super.tampilkanInfo()'.
 * - Method Overriding: Meng-override method tampilkanInfo() dan abstract method proses(double suhu).
 * - Logika State & Percabangan: Implementasi proses() memiliki logika if/else untuk mengubah kondisi suhu.
 * - Constructor Overloading: Memiliki dua variasi constructor.
 */
public class SensorSuhu extends PerangkatIoT {

    // Konstanta global (Immutability): Batas ambang suhu panas bernilai tetap 30.0°C
    public static final double BATAS_SUHU_PANAS = 30.0;

    // Atribut privat (Enkapsulasi)
    private String satuan;
    private double suhuTerakhir;
    private String kondisiSuhu;

    /**
     * Constructor 1 (Overloading):
     * Menggunakan satuan default "°C".
     * Menggunakan Super Chaining 'super(idPerangkat, namaPerangkat, true)'
     * karena sensor IoT umumnya langsung aktif saat dinyalakan.
     */
    public SensorSuhu(String idPerangkat, String namaPerangkat) {
        super(idPerangkat, namaPerangkat, true); // Super Chaining ke constructor superclass
        this.satuan = "°C";
        this.suhuTerakhir = 0.0;
        this.kondisiSuhu = "BELUM DIUKUR";
    }

    /**
     * Constructor 2 (Overloading):
     * Memungkinkan penentuan satuan suhu secara spesifik saat instansiasi.
     */
    public SensorSuhu(String idPerangkat, String namaPerangkat, String satuan) {
        super(idPerangkat, namaPerangkat, true); // Super Chaining
        this.satuan = satuan;
        this.suhuTerakhir = 0.0;
        this.kondisiSuhu = "BELUM DIUKUR";
    }

    // --- Getter dan Setter (Enkapsulasi) ---

    public String getSatuan() {
        return this.satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public double getSuhuTerakhir() {
        return this.suhuTerakhir;
    }

    public void setSuhuTerakhir(double suhuTerakhir) {
        this.suhuTerakhir = suhuTerakhir;
    }

    public String getKondisiSuhu() {
        return this.kondisiSuhu;
    }

    public void setKondisiSuhu(String kondisiSuhu) {
        this.kondisiSuhu = kondisiSuhu;
    }

    /**
     * Method Fungsi: Mengembalikan nilai boolean apakah suhu ruangan dalam kondisi panas.
     * 
     * @return true jika suhu >= BATAS_SUHU_PANAS, false jika sebaliknya.
     */
    public boolean isPanas() {
        return this.suhuTerakhir >= BATAS_SUHU_PANAS;
    }

    /**
     * Implementasi Abstract Method dari PerangkatIoT.
     * Berisi logika percabangan if/else untuk membaca kondisi suhu dan
     * mengubah state internal atribut 'kondisiSuhu' dan 'suhuTerakhir'.
     * 
     * Aturan:
     * - Suhu >= 30.0°C : kondisi = PANAS
     * - Suhu < 30.0°C  : kondisi = NORMAL
     * 
     * @param suhu Nilai suhu yang diinputkan pengguna.
     */
    @Override
    public void proses(double suhu) {
        this.suhuTerakhir = suhu;

        // Logika state dan percabangan
        if (suhu >= BATAS_SUHU_PANAS) {
            this.kondisiSuhu = "PANAS";
        } else {
            this.kondisiSuhu = "NORMAL";
        }
    }

    /**
     * Method Overriding:
     * Mengubah perilaku method tampilkanInfo() dari superclass.
     * Menggunakan 'super.tampilkanInfo()' untuk mencetak info dasar terlebih dahulu,
     * lalu menambahkan rincian spesifik sensor suhu.
     */
    @Override
    public void tampilkanInfo() {
        super.tampilkanInfo(); // Super Chaining memanggil method superclass
        System.out.println("Tipe Sensor    : Sensor Suhu Ruangan");
        System.out.println("Suhu Terakhir  : " + this.suhuTerakhir + " " + this.satuan);
        System.out.println("Kondisi Suhu   : " + this.kondisiSuhu);
        System.out.println("Batas Panas    : " + BATAS_SUHU_PANAS + " " + this.satuan);
    }
}

