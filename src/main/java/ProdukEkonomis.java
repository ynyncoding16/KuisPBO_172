public class ProdukEkonomis extends Produk {
    private static final long serialVersionUID = 1L;

    public ProdukEkonomis(String kode, String nama, int stok) {
        super(kode, nama, stok);
    }

    @Override
    public String getKategoriLayanan() {
        return "Ekonomis - harga hemat tanpa garansi";
    }
}