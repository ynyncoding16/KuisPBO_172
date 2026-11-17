public class ProdukReguler extends Produk {
    private static final long serialVersionUID = 1L;

    public ProdukReguler(String kode, String nama, int stok) {
        super(kode, nama, stok);
    }

    @Override
    public String getKategoriLayanan() {
        return "Reguler - garansi standar & pengiriman biasa";
    }
}