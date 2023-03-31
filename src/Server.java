public class Server {
    private String name;
    private int id;
    private int cores;

    public Server() {
        this.name = "error";
        this.id = -1;
        this.cores = -1;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCores(int cores) {
        this.cores = cores;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getCores() {
        return cores;
    }

    @Override
    public String toString() {
        return name + " " + id + " " + cores;
    }
}
