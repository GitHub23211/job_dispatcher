public class Server {
    private String name;
    private String id;
    private int cores;

    public Server(String name, String id, int cores) {
        this.name = name;
        this.id = id;
        this.cores = cores;
    }

    public Server() {
        name = "error";
        id = "0";
        cores = 0;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCores(int cores) {
        this.cores = cores;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getNameAndID() {
        return name+id;
    }

    public int getCores() {
        return cores;
    }

    @Override
    public String toString() {
        return name + " " + id + " " + cores;
    }
}
