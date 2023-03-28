public abstract class Scheduler {
    Buffer buffer;
    Message msg;
    String largestServerName;
    Parser parse;

    Scheduler(Buffer buffer, Message msg, String largestServerName, Parser parse) {
        this.buffer = buffer;
        this.msg = msg;
        this.largestServerName = largestServerName;
        this.parse = parse;
    }

    public abstract void execute();
}
