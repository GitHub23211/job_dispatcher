import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetsParser extends Parser {

    private Buffer buffer;
    private Message msg;

    GetsParser(Buffer buffer, Message msg) {
        super();
        this.buffer = buffer;
        this.msg = msg;
    }

    public void parse() {
        getServers();
        cleanServerList();
    }

    private void getServers() {
        try {
            msg.send("GETS All");
            if(buffer.contains("DATA")) {
                msg.send("OK");
                while(buffer.isReady()) {
                    buffer.update();
                    servers.add(parseGetsAll(buffer.get()));
                }
                msg.send("OK");
            }
            msg.send("REDY");
        } catch (Exception e) {System.out.println("Error @ GETS All request: " + e);}
    }

    private Server parseGetsAll(String msg) {
        String reg = "([^ ]+)([ ])([^ ]+)([ ])([^ ]+)([ ])([^ ]+)([ ])([^ ]+)";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(msg);
        Server server = new Server();
        if(m.find()) {
            server.setName(m.group(1));
            server.setId(Integer.parseInt(m.group(3)));
            server.setCores(Integer.parseInt(m.group(9)));
            return server;
        }
         return server;
    }

    private void cleanServerList() {
        String name = "";
        for(int i = servers.size() - 1; i >= 0; i--) {
            Server s = servers.get(i);
            System.out.println(s.toString());
            if(!s.getName().equals(name)) {
                name = s.getName();
            }
            else {
                servers.remove(i);
            }
        }
    }
}
