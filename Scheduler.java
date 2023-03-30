import java.util.ArrayList; 

public abstract class Scheduler {
    Buffer buffer;
    Message msg;
    String largestServerName;
    String maxLargestServer;
    ArrayList<ArrayList<String>> servers;

    Scheduler(Buffer buffer, Message msg) {
        this.buffer = buffer;
        this.msg = msg;
        this.servers = new ArrayList<ArrayList<String>>();
        this.largestServerName = "";
        this.maxLargestServer = "0";
    }

    public abstract void execute();

    public void getLargestServer() {
        try {
            msg.send("GETS All");
            if(buffer.contains("DATA")) {
                msg.send("OK");
                while(buffer.isReady()) {
                    buffer.update();
                    servers.add(Parser.getServerInfo(buffer.get()));
                }
                System.out.println(servers);
                //largestServerName = 
                msg.send("OK");
            }
            msg.send("REDY");
        } catch (Exception e) {System.out.println("Error @ GETS All request: " + e);}
    }
    
    public void findLargestServers() {
    	    int maxCore = 0;
	for (int i = servers.size() - 1; i >= 0; i--) {
		if(Integer.parseInt(servers[2]) >= maxCore) {
			maxCore = servers[2];
			
		}
	    
	}
    }
}
