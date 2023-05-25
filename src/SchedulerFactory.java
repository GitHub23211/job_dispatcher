public class SchedulerFactory {
    private Buffer buffer;
    private Message msg;

    SchedulerFactory(Buffer buffer, Message msg) {
        this.buffer = buffer;
        this.msg = msg;
    }
    
    public Scheduler getAlgorithm(String algo) {
        switch(algo) {
            case "bgabq":
                return new GetsAvailBestQ(buffer, msg);
            default:
                return new GetsAvailQ(buffer, msg);
        }
    }
}
