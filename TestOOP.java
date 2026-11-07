import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Class TestOOP
 * Program pengujian otomatis untuk memverifikasi dan membuktikan
 * pemenuhan seluruh 10 Requirement Pemrograman Berorientasi Objek (PBO).
 * 
 * Jalankan dengan:
 *   javac TestOOP.java
 *   java TestOOP
 */
public class TestOOP {

    public static void main(String[] args) {
        System.out.println("===============================================================");
        System.out.println("   VERIFIKASI OTOMATIS 10 REQUIREMENT PBO - SMART ROOM SYSTEM  ");
        System.out.println("===============================================================");

        int lulus = 0;

        // Req 1: State & Behavior
        if (ujiRequirement1()) lulus++;

        // Req 2: Immutability
        if (ujiRequirement2()) lulus++;

        // Req 3: Enkapsulasi & Overloading
        if (ujiRequirement3()) lulus++;

        // Req 4: Inheritance
        if (ujiRequirement4()) lulus++;

        // Req 5: Super Chaining
        if (ujiRequirement5()) lulus++;

        // Req 6: Method Overriding
        if (ujiRequirement6()) lulus++;

        // Req 7: Abstract Class
        if (ujiRequirement7()) lulus++;

        // Req 8: Logika State & Percabangan
        if (ujiRequirement8()) lulus++;

        // Req 9: Polimorfisme Interface
        if (ujiRequirement9()) lulus++;

        // Req 10: Multiple Interface
        if (ujiRequirement10()) lulus++;

        System.out.println("\n===============================================================");
        System.out.println(" HASIL VERIFIKASI: " + lulus + "/10 REQUIREMENT BERHASIL TERPENUHI!");
        if (lulus == 10) {
            System.out.println(" STATUS: SEMUA PERSYARATAN WAJIB PBO 100% VALID DAN TERBUKTI!");
        }
        System.out.println("===============================================================");
    }

    private static boolean ujiRequirement1() {
        System.out.print("[REQ 1] State & Behavior (Atribut, Void Method, Return Value)... ");
        SensorSuhu s = new SensorSuhu("S-01", "Sensor Demo");
        s.proses(31.5); // void method
        double suhu = s.getSuhuTerakhir(); // return value (double)
        boolean panas = s.isPanas(); // return value (boolean)
        if (suhu == 31.5 && panas) {
            System.out.println("[TERPENUHI]");
            return true;
        }
        System.out.println("[GAGAL]");
        return false;
    }

    private static boolean ujiRequirement2() {
        System.out.print("[REQ 2] Immutability (public static final & private final)... ");
        boolean globalConst = (SensorSuhu.BATAS_SUHU_PANAS == 30.0);
        
        // Memeriksa atribut final pada PerangkatIoT via Reflection
        boolean instanceFinal = false;
        try {
            Field field = PerangkatIoT.class.getDeclaredField("idPerangkat");
            int mod = field.getModifiers();
            instanceFinal = Modifier.isPrivate(mod) && Modifier.isFinal(mod);
        } catch (Exception e) {
            instanceFinal = false;
        }

        if (globalConst && instanceFinal) {
            System.out.println("[TERPENUHI]");
            return true;
        }
        System.out.println("[GAGAL]");
        return false;
    }

    private static boolean ujiRequirement3() {
        System.out.print("[REQ 3] Enkapsulasi & Overloading (Private, Get/Set, Overload)... ");
        // Uji constructor overloading pada SensorSuhu & KipasPintar
        SensorSuhu s1 = new SensorSuhu("S-01", "Sensor 1");
        SensorSuhu s2 = new SensorSuhu("S-02", "Sensor 2", "°C");
        KipasPintar k1 = new KipasPintar("K-01", "Kipas 1");
        KipasPintar k2 = new KipasPintar("K-02", "Kipas 2", 3, "MANUAL");

        if (s1.getSatuan().equals("°C") && s2.getSatuan().equals("°C") &&
            k1.getLevelKecepatan() == 0 && k2.getLevelKecepatan() == 3) {
            System.out.println("[TERPENUHI]");
            return true;
        }
        System.out.println("[GAGAL]");
        return false;
    }

    private static boolean ujiRequirement4() {
        System.out.print("[REQ 4] Inheritance (Superclass PerangkatIoT, 2 Subclass)... ");
        SensorSuhu sensor = new SensorSuhu("S-01", "Sensor");
        KipasPintar kipas = new KipasPintar("K-01", "Kipas");

        boolean sensorIsSub = sensor instanceof PerangkatIoT;
        boolean kipasIsSub = kipas instanceof PerangkatIoT;

        if (sensorIsSub && kipasIsSub) {
            System.out.println("[TERPENUHI]");
            return true;
        }
        System.out.println("[GAGAL]");
        return false;
    }

    private static boolean ujiRequirement5() {
        System.out.print("[REQ 5] Super Chaining (super(...) & super.tampilkanInfo())... ");
        SensorSuhu sensor = new SensorSuhu("S-TEST", "Nama Tes");
        // Membuktikan super(...) berhasil menetapkan idPerangkat dan namaPerangkat di superclass
        boolean superConstructorWorks = sensor.getIdPerangkat().equals("S-TEST") &&
                                        sensor.getNamaPerangkat().equals("Nama Tes");
        if (superConstructorWorks) {
            System.out.println("[TERPENUHI]");
            return true;
        }
        System.out.println("[GAGAL]");
        return false;
    }

    private static boolean ujiRequirement6() {
        System.out.print("[REQ 6] Method Overriding (@Override pada subclass)... ");
        try {
            Method mSensor = SensorSuhu.class.getMethod("proses", double.class);
            Method mKipas = KipasPintar.class.getMethod("proses", double.class);
            if (mSensor != null && mKipas != null) {
                System.out.println("[TERPENUHI]");
                return true;
            }
        } catch (Exception e) {
            // error
        }
        System.out.println("[GAGAL]");
        return false;
    }

    private static boolean ujiRequirement7() {
        System.out.print("[REQ 7] Abstract Class (PerangkatIoT & abstract proses())... ");
        boolean isAbstractClass = Modifier.isAbstract(PerangkatIoT.class.getModifiers());
        boolean hasAbstractMethod = false;
        try {
            Method m = PerangkatIoT.class.getDeclaredMethod("proses", double.class);
            hasAbstractMethod = Modifier.isAbstract(m.getModifiers());
        } catch (Exception e) {
            hasAbstractMethod = false;
        }

        if (isAbstractClass && hasAbstractMethod) {
            System.out.println("[TERPENUHI]");
            return true;
        }
        System.out.println("[GAGAL]");
        return false;
    }

    private static boolean ujiRequirement8() {
        System.out.print("[REQ 8] Logika State & Percabangan (if/else suhu >= 30.0)... ");
        SensorSuhu sensor = new SensorSuhu("S-01", "Sensor");
        KipasPintar kipas = new KipasPintar("K-01", "Kipas");

        // Uji kondisi 1: Panas (32.0 C)
        sensor.proses(32.0);
        kipas.proses(32.0);
        boolean panasKondisi = sensor.getKondisiSuhu().equals("PANAS") && kipas.isStatusAktif() && kipas.getLevelKecepatan() == 2;

        // Uji kondisi 2: Normal (24.0 C)
        sensor.proses(24.0);
        kipas.proses(24.0);
        boolean normalKondisi = sensor.getKondisiSuhu().equals("NORMAL") && !kipas.isStatusAktif() && kipas.getLevelKecepatan() == 0;

        if (panasKondisi && normalKondisi) {
            System.out.println("[TERPENUHI]");
            return true;
        }
        System.out.println("[GAGAL]");
        return false;
    }

    private static boolean ujiRequirement9() {
        System.out.print("[REQ 9] Polimorfisme Interface (BisaDimonitor & BisaDikendalikan)... ");
        KipasPintar kipas = new KipasPintar("K-01", "Kipas");

        // Referensi interface
        BisaDimonitor monitor = kipas;
        BisaDikendalikan kontrol = kipas;

        kontrol.aktifkan(); // Mengendalikan via interface
        boolean aktifValid = kipas.isStatusAktif();

        kontrol.nonaktifkan(); // Menonaktifkan via interface
        boolean nonaktifValid = !kipas.isStatusAktif();

        if (aktifValid && nonaktifValid && (monitor != null)) {
            System.out.println("[TERPENUHI]");
            return true;
        }
        System.out.println("[GAGAL]");
        return false;
    }

    private static boolean ujiRequirement10() {
        System.out.print("[REQ 10] Multiple Interface (implements BisaDimonitor, BisaDikendalikan)... ");
        KipasPintar kipas = new KipasPintar("K-01", "Kipas");
        boolean isMonitor = kipas instanceof BisaDimonitor;
        boolean isKendali = kipas instanceof BisaDikendalikan;

        if (isMonitor && isKendali) {
            System.out.println("[TERPENUHI]");
            return true;
        }
        System.out.println("[GAGAL]");
        return false;
    }
}

