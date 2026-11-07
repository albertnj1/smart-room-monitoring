/**
 * Interface BisaDikendalikan
 * Mendefinisikan kontrak perilaku untuk perangkat yang dapat dikendalikan (dinyalakan/dimatikan).
 * Bagian dari penerapan konsep Abstraction dan Interface Polymorphism.
 */
public interface BisaDikendalikan {

    /**
     * Mengaktifkan perangkat.
     */
    void aktifkan();

    /**
     * Menonaktifkan perangkat.
     */
    void nonaktifkan();
}

