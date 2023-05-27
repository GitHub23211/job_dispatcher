public class Server {
    public String name;
    public String id;
    public String state;
    public int cores;
    public int mem;
    public int disk;

    public Server(String name, String id, String state, int cores, int mem, int disk) {
        this.name = name;
        this.id = id;
        this.cores = cores;
        this.mem = mem;
        this.disk = disk;
        this.state = state;
    }

    public Server() {
        name = "error";
        id = "0";
        cores = 0;
        mem = 0;
        disk = 0;
        state = "";
    }

    public boolean isValid() {
        return !name.contains("error");
    }

    public String getNameAndID() {
        return name+id;
    }

    @Override
    public String toString() {
        return name + " " + id + " " + cores;
    }
}
