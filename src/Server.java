public class Server {
    public String name;
    public String id;
    public int cores;

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

    public String getNameAndID() {
        return name+id;
    }

    @Override
    public String toString() {
        return name + " " + id + " " + cores;
    }
}
