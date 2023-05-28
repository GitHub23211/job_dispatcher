public class Job {
    public String name;
    public String id;
    public int cores;
    public int mem;
    public int disk;
    public int coreFit;
    public int currentFit = -1;

    Job(String name, String id, int cores, int mem, int disk) {
        this.name = name;
        this.id = id;
        this.cores = cores;
        this.mem = mem;
        this.disk = disk;
    }

    Job(String name, String id, int cores) {
        this.name = name;
        this.id = id;
        this.cores = cores;
        this.mem = 0;
        this.disk = 0;
    }

    Job() {
        this.name = "empty";
        this.id = "0";
        this.cores = 0;
        this.mem = 0;
        this.disk = 0;
    }

    public boolean coreFit(Server server) {
        int coreCompare = server.cores - cores;
        if((coreCompare <= currentFit || currentFit == -1) && coreCompare >= 0) {
            currentFit = coreCompare;
            return true;
        }
        return false;
    }

    public String jobInfo(){
        return cores + " " + mem + " " + disk;
    }
}
