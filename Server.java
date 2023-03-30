public class Server {
    private String name;
    private String id;
    private String cores;

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCores(String cores) {
        this.cores = cores;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getCores() {
        return cores;
    }

    public String getAll() {
        return name + " " + id + " " + cores;
    }
}
