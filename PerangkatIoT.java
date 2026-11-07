/**
 * Abstract Class PerangkatIoT
 * Menjadi superclass (kelas induk) untuk semua perangkat berbasis IoT dalam sistem.
 * 
 * Penerapan Konsep PBO:
 * - Abstract Class: Menggunakan keyword 'abstract', tidak dapat diinstansiasi langsung.
 * - Immutability: Atribut 'idPerangkat' bersifat 'private final' (kekal, hanya punya getter).
 * - Enkapsulasi: Semua atribut private, diakses melalui getter dan setter.
 * - Constructor Overloading: Memiliki dua variasi constructor.
 * - Abstract Method: Mendefinisikan method proses(double suhu) tanpa implementasi.
 */
public abstract class PerangkatIoT {

    // Atribut instans bersifat kekal (Immutability) - tidak dapat diubah setelah inisialisasi
    private final String idPerangkat;

    // Atribut dengan modifier private (Enkapsulasi)
    private String namaPerangkat;
    private boolean statusAktif;

    /**
     * Constructor 1 (Overloading):
     * Menginisialisasi ID dan nama perangkat, default statusAktif = false.
     * Menggunakan keyword 'this' untuk membedakan atribut dan parameter.
     */
    public PerangkatIoT(String idPerangkat, String namaPerangkat) {
        this.idPerangkat = idPerangkat;
        this.namaPerangkat = namaPerangkat;
        this.statusAktif = false;
    }

    /**
     * Constructor 2 (Overloading):
     * Menginisialisasi ID, nama perangkat, dan status aktif awal secara spesifik.
     */
    public PerangkatIoT(String idPerangkat, String namaPerangkat, boolean statusAktif) {
        this.idPerangkat = idPerangkat;
        this.namaPerangkat = namaPerangkat;
        this.statusAktif = statusAktif;
    }

    // --- Getter dan Setter (Enkapsulasi) ---

    /**
     * Getter untuk idPerangkat.
     * Tidak ada setter karena idPerangkat bersifat final (Immutability).
     */
    public String getIdPerangkat() {
        return this.idPerangkat;
    }

    public String getNamaPerangkat() {
        return this.namaPerangkat;
    }

    public void setNamaPerangkat(String namaPerangkat) {
        this.namaPerangkat = namaPerangkat;
    }

    public boolean isStatusAktif() {
        return this.statusAktif;
    }

    public void setStatusAktif(boolean statusAktif) {
        this.statusAktif = statusAktif;
    }

    /**
     * Method Prosedur (void) untuk menampilkan informasi dasar perangkat.
     * Method ini akan di-override oleh subclass (Method Overriding) dan
     * dipanggil kembali oleh subclass menggunakan super.tampilkanInfo().
     */
    public void tampilkanInfo() {
        System.out.println("ID Perangkat   : " + this.idPerangkat);
        System.out.println("Nama Perangkat : " + this.namaPerangkat);
        System.out.println("Status Daya    : " + (this.statusAktif ? "AKTIF / ON" : "NONAKTIF / OFF"));
    }

    /**
     * Abstract Method:
     * Wajib diimplementasikan oleh setiap subclass turunan dengan logika state masing-masing.
     * Parameter suhu merepresentasikan nilai masukan dari sistem IoT.
     * 
     * @param suhu Nilai suhu ruangan saat ini dalam derajat Celcius.
     */
    public abstract void proses(double suhu);
}

