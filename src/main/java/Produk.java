import java.io.Serializable;

public abstract class Produk implements Serializable {
    private static final long serialVersionUID = 1L;

    private String kode;
    private String nama;
    private int stok;
    private String catatanTerakhir;

    public Produk(String kode, String nama, int stok) {
        this.setKode(kode);
        this.setNama(nama);
        this.setStok(stok);
        this.catatanTerakhir = "-";
    }

    // ===== Getter & Setter bervalidasi =====
    public String getKode() {
        return this.kode;
    }

    public void setKode(String kode) {
        if (kode == null || kode.trim().isEmpty()) {
            throw new IllegalArgumentException("Kode produk tidak boleh kosong");
        }
        this.kode = kode.trim();
    }

    public String getNama() {
        return this.nama;
    }

    public void setNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama produk tidak boleh kosong");
        }
        this.nama = nama.trim();
    }

    public int getStok() {
        return this.stok;
    }

    public void setStok(int stok) {
        if (stok < 0) {
            throw new IllegalArgumentException("Stok tidak boleh negatif");
        }
        this.stok = stok;
    }

    public String getCatatanTerakhir() {
        return this.catatanTerakhir;
    }

    // ===== Method Overloading (compile-time polymorphism) =====
    public void kurangiStok(int jumlah) throws StokTidakCukupException {
        kurangiStok(jumlah, "Tanpa catatan");
    }

    public void kurangiStok(int jumlah, String catatan) throws StokTidakCukupException {
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah pengurangan harus lebih dari 0");
        }
        if (jumlah > this.stok) {
            throw new StokTidakCukupException("Stok " + this.nama
                    + " tidak cukup. Diminta: " + jumlah + ", tersedia: " + this.stok);
        }
        this.stok = this.stok - jumlah;
        this.catatanTerakhir = catatan;
    }

    // ===== Abstract method (di-override berbeda di tiap subclass) =====
    public abstract String getKategoriLayanan();

    public String getInfo() {
        return "Produk [" + this.kode + "] " + this.nama
                + " | Stok: " + this.stok
                + " | Kategori: " + getKategoriLayanan();
    }
}