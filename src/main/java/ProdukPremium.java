public class ProdukPremium extends Produk {
    private static final long serialVersionUID = 1L;

    public ProdukPremium(String kode, String nama, int stok) {
        super(kode, nama, stok);
    }

    @Override
    public String getKategoriLayanan() {
        return "Premium - garansi resmi & pengiriman prioritas";
    }
}