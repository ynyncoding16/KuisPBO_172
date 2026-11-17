import java.io.Serializable;

public abstract class Aktor implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String nama;

    public Aktor(String id, String nama) {
        this.setId(id);
        this.setNama(nama);
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID aktor tidak boleh kosong");
        }
        this.id = id.trim();
    }

    public String getNama() {
        return this.nama;
    }

    public void setNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama aktor tidak boleh kosong");
        }
        this.nama = nama.trim();
    }

    public abstract String getInfo();
}