public class Server {
    public String name;
    public String id;
    public String state;
    public int cores;

    public Server(String name, String id, String state, int cores) {
        this.name = name;
        this.id = id;
        this.cores = cores;
        this.state = state;
    }

    public Server() {
        name = "error";
        id = "0";
        cores = 0;
        state = "";
    }

    public boolean isValid() {
        return name.contains("error");
    }

    public String getNameAndID() {
        return name+id;
    }

    @Override
    public String toString() {
        return name + " " + id + " " + cores;
    }
}
