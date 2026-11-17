public class MainToko {
    public static void main(String[] args) {
        SistemToko toko = new SistemToko("Toko Sukses Makmur");

        toko.tambahPelanggan("ACT-01", "Andi");
        toko.tambahPelanggan("ACT-02", "Rina");

        toko.tambahProduk("OBJ-A", "Laptop Premium", 10);
        toko.tambahProduk("OBJ-B", "Kemeja Reguler", 2);
        toko.tambahProduk("OBJ-C", "Snack Ekonomis", 50);

        System.out.println("--- MEMULAI PEMBACAAN FILE TRANSAKSI ---");

        try {
            toko.jalankanTransaksiDariFile("input_transaksi.txt");
        } catch (Exception e) {
            System.out.println("[SISTEM FATAL ERROR] File gagal dibaca/diproses: " + e.getMessage());
        }

        toko.simpanLaporanAkhir();
    }
}