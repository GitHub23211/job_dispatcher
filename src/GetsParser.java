import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetsParser extends Parser {

    private Buffer buffer;
    private Message msg;
    private int currCores;

    GetsParser(Buffer buffer, Message msg) {
        super();
        this.buffer = buffer;
        this.msg = msg;
        this.currCores = 0;
    }

    @Override
    public void parse() {
        try {
            msg.send("GETS All");
            if(buffer.contains("DATA")) {
                msg.send("OK");
                while(buffer.isReady()) {
                    getLargestServer(buffer.get());
                    buffer.update();
                }
                getLargestServer(buffer.get());
                msg.send("OK");
            }
            msg.send("REDY");
        } catch (Exception e) {System.out.println("Error @ GETS All request: " + e);}
    }

    private void getLargestServer(String msg) {
        String reg = "(?<name>[^ ]+)[ ](?<id>[^ ]+)[ ]([^ ]+)[ ]([^ ]+)[ ](?<cores>[^ ]+)";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(msg);
        if(m.find()) {
            if(Integer.parseInt(m.group("cores")) > currCores || m.group("name").equals(name)) {
                currCores = Integer.parseInt(m.group("cores"));
                name = m.group("name");
                max = Integer.parseInt(m.group("id")) + 1;
            }
        }
    }
}
