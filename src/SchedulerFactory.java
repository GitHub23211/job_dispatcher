public class SchedulerFactory {
    private Buffer buffer;
    private Message msg;

    SchedulerFactory(Buffer buffer, Message msg) {
        this.buffer = buffer;
        this.msg = msg;
    }
    
    public Scheduler getAlgorithm(String algo) {
        switch(algo) {
            case "e":
                return new GetsAvailExhaust(buffer, msg);
            case "q":
                return new GetsAvailQ(buffer, msg);
            default:
                return new GetsAvailExhaust(buffer, msg);
        }
    }
}
