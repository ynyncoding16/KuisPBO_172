import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class SistemToko implements Serializable {
    private static final long serialVersionUID = 1L;

    private String namaToko;

    // Aggregation: sistem "memiliki" kumpulan pelanggan, produk, dan transaksi
    private HashMap<String, Aktor> daftarPelanggan = new HashMap<String, Aktor>();
    private ArrayList<Produk> daftarProduk = new ArrayList<Produk>();
    private ArrayList<Transaksi> daftarTransaksi = new ArrayList<Transaksi>();

    public SistemToko(String namaToko) {
        this.setNamaToko(namaToko);
    }

    public String getNamaToko() {
        return this.namaToko;
    }

    public void setNamaToko(String namaToko) {
        if (namaToko == null || namaToko.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama toko tidak boleh kosong");
        }
        this.namaToko = namaToko.trim();
    }

    public void tambahPelanggan(String id, String nama) {
        daftarPelanggan.put(id, new Pelanggan(id, nama));
    }

    // Upcasting: objek subclass disimpan sebagai tipe Produk
    public void tambahProduk(String kode, String nama, int stok) {
        Produk baru;
        if (nama.contains("Premium")) {
            baru = new ProdukPremium(kode, nama, stok);
        } else if (nama.contains("Ekonomis")) {
            baru = new ProdukEkonomis(kode, nama, stok);
        } else {
            // "Reguler" dan nama lain dianggap produk reguler
            baru = new ProdukReguler(kode, nama, stok);
        }
        daftarProduk.add(baru);
    }

    private Pelanggan cariPelanggan(String id) throws PelangganTidakDitemukanException {
        Aktor a = daftarPelanggan.get(id);
        if (a == null || !(a instanceof Pelanggan)) {
            throw new PelangganTidakDitemukanException(
                    "Pelanggan dengan ID " + id + " tidak terdaftar");
        }
        return (Pelanggan) a;
    }

    private Produk cariProduk(String kode) throws ProdukTidakDitemukanException {
        for (Produk p : daftarProduk) {
            if (p.getKode().equals(kode)) {
                return p;
            }
        }
        throw new ProdukTidakDitemukanException(
                "Produk dengan kode " + kode + " tidak ditemukan");
    }

    // Dynamic binding: getInfo() memanggil getKategoriLayanan() milik subclass
    private void tampilkanKatalog() {
        System.out.println("=== KATALOG " + namaToko + " ===");
        for (Produk p : daftarProduk) {
            System.out.println(p.getInfo());
        }
        System.out.println();
    }

    // Membaca file dengan File + Scanner, memisah transaksi dengan '|'
    public void jalankanTransaksiDariFile(String namaFile) throws FileNotFoundException {
        tampilkanKatalog();

        File file = new File(namaFile);
        Scanner scanner = new Scanner(file); // FileNotFoundException jika file tidak ada
        StringBuilder isiFile = new StringBuilder();
        try {
            while (scanner.hasNextLine()) {
                isiFile.append(scanner.nextLine());
            }
        } finally {
            scanner.close();
        }

        String[] daftarData = isiFile.toString().split("\\|");
        int berhasil = 0;
        int gagal = 0;

        for (int i = 0; i < daftarData.length; i++) {
            String data = daftarData[i].trim();
            if (data.isEmpty()) {
                continue;
            }
            try {
                prosesSatuTransaksi(data);
                berhasil++;
            } catch (TokoException e) {
                System.out.println("[GAGAL] Transaksi ke-" + (i + 1) + ": " + e.getMessage());
                gagal++;
            }
        }
        System.out.println("Ringkasan: " + berhasil + " berhasil, " + gagal + " gagal.");
    }

    // Contoh data: ID:ACT-01;KD:OBJ-A;JML:3;
    private void prosesSatuTransaksi(String data) throws TokoException {
        String idPelanggan = null;
        String kodeProduk = null;
        String teksJumlah = null;

        String[] atribut = data.split(";");
        for (String a : atribut) {
            a = a.trim();
            if (a.isEmpty()) {
                continue;
            }
            String[] kv = a.split(":");
            if (kv.length != 2) {
                throw new FormatTransaksiException("Format atribut salah: '" + a + "'");
            }
            String kunci = kv[0].trim();
            String nilai = kv[1].trim();

            if (kunci.equals("ID")) {
                idPelanggan = nilai;
            } else if (kunci.equals("KD")) {
                kodeProduk = nilai;
            } else if (kunci.equals("JML")) {
                teksJumlah = nilai;
            } else {
                throw new FormatTransaksiException("Kunci tidak dikenal: '" + kunci + "'");
            }
        }

        if (idPelanggan == null || kodeProduk == null || teksJumlah == null) {
            throw new FormatTransaksiException("Data transaksi tidak lengkap: " + data);
        }

        int jumlah;
        try {
            jumlah = Integer.parseInt(teksJumlah);
        } catch (NumberFormatException e) {
            throw new FormatTransaksiException("Jumlah bukan angka: '" + teksJumlah + "'");
        }
        if (jumlah <= 0) {
            throw new FormatTransaksiException("Jumlah harus lebih dari 0");
        }

        Pelanggan pelanggan = cariPelanggan(idPelanggan);
        Produk produk = cariProduk(kodeProduk);

        // Overloading: memakai versi dengan parameter catatan
        produk.kurangiStok(jumlah, "Dibeli oleh " + pelanggan.getId());

        daftarTransaksi.add(new Transaksi(pelanggan, produk, jumlah));

        // Dynamic binding: getKategoriLayanan() berbeda untuk tiap subclass
        System.out.println("[OK] " + pelanggan.getNama() + " membeli " + jumlah
                + " x " + produk.getNama()
                + " (" + produk.getKategoriLayanan() + ")"
                + " | Sisa stok: " + produk.getStok());
    }

    public void simpanLaporanAkhir() {
        // A. Object Serialization
        try {
            FileOutputStream fos = new FileOutputStream("archive_mem.bin");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            try {
                oos.writeObject(this);
            } finally {
                oos.close();
            }
            System.out.println("Serialization berhasil: archive_mem.bin");
        } catch (IOException e) {
            System.out.println("Gagal serialization: " + e.getMessage());
        }

        // B. File I/O teks: loop ArrayList Produk
        try {
            FileWriter writer = new FileWriter("laporan.txt");
            try {
                writer.write("LAPORAN SISA STOK PRODUK\n");
                writer.write("================================\n");
                for (Produk p : daftarProduk) {
                    writer.write("Kode Produk : " + p.getKode() + "\n");
                    writer.write("Nama Produk : " + p.getNama() + "\n");
                    writer.write("Sisa Stok   : " + p.getStok() + "\n");
                    writer.write("\n");
                }
            } finally {
                writer.close();
            }
            System.out.println("Laporan berhasil: laporan.txt");
        } catch (IOException e) {
            System.out.println("Gagal menulis laporan: " + e.getMessage());
        }
    }
}