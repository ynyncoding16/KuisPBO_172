import java.io.Serializable;

public class Transaksi implements Serializable {
    private static final long serialVersionUID = 1L;

    private Pelanggan pelanggan; // Association
    private Produk produk;       // Association
    private int jumlah;

    public Transaksi(Pelanggan pelanggan, Produk produk, int jumlah) {
        this.setPelanggan(pelanggan);
        this.setProduk(produk);
        this.setJumlah(jumlah);
    }

    public Pelanggan getPelanggan() {
        return this.pelanggan;
    }

    public void setPelanggan(Pelanggan pelanggan) {
        if (pelanggan == null) {
            throw new IllegalArgumentException("Pelanggan tidak boleh null");
        }
        this.pelanggan = pelanggan;
    }

    public Produk getProduk() {
        return this.produk;
    }

    public void setProduk(Produk produk) {
        if (produk == null) {
            throw new IllegalArgumentException("Produk tidak boleh null");
        }
        this.produk = produk;
    }

    public int getJumlah() {
        return this.jumlah;
    }

    public void setJumlah(int jumlah) {
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah transaksi harus lebih dari 0");
        }
        this.jumlah = jumlah;
    }
}