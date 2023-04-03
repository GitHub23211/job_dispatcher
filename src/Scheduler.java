import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Scheduler {
    Buffer buffer;
    Message msg;
    Parser parser;
    String largestServerName;
    int maxLargestServer;

    Scheduler(Buffer buffer, Message msg, Parser parser) {
        this.buffer = buffer;
        this.msg = msg;
        this.parser = parser;
        this.largestServerName = "";
        this.maxLargestServer = 0;
    }

    public abstract void execute();

    public void setServers() {
        parser.parse();
        largestServerName = parser.getServerName();
        maxLargestServer = parser.getMaxServerCount();
    }

    private String getJobId(String msg) {
        String reg = "([a-zA-Z ])+[0-9]+([ ]+[0-9]+)";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(msg);
        if(m.find()) {
            return m.group(2);
        }
        return "";
    }

    public String getJob() {
        return "SCHD" + getJobId(buffer.get()) + " " + largestServerName + " ";
    }
    
}
