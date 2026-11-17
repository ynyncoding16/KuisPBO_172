public class Pelanggan extends Aktor {
    private static final long serialVersionUID = 1L;

    public Pelanggan(String id, String nama) {
        super(id, nama);
    }

    @Override
    public String getInfo() {
        return "Pelanggan [" + getId() + "] " + getNama();
    }
}