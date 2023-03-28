import java.net.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Client {
    private String ip;
    private int port;
    private Socket client;
    private BufferedReader input;
    private DataOutputStream output;
    private String buffer;
    private String largestServer;
    
    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.buffer = "";
        this.initalise();
    }

    private void initalise() {
        try {
            this.client = new Socket(this.ip, this.port);
            this.input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            this.output = new DataOutputStream(client.getOutputStream());
        } catch (Exception e) {System.out.println("Error @ initalisaiton: " + e);}       
    }

    public void auth() {
        try {
            String[] msgs = {"HELO\n", "AUTH user\n", "REDY\n"};
            for(int i = 0; i < msgs.length; i++) {
                this.sendMsgs(msgs[i]);
                this.printMsgs();
            }
        } catch (Exception e) {System.out.println("Error @ authentication: " + e);}
    }

    public void getServerInfo() {
        try {
            this.sendMsgs("GETS All\n");
            this.printMsgs();
            if(this.buffer.contains("DATA")) {
                this.sendMsgs("OK\n");
                this.buffer = this.input.readLine();
                while(this.input.ready()) {
                    this.buffer = this.input.readLine();
                }
                this.getFirstLargestServer(buffer);
                System.out.println(this.largestServer);
                this.sendMsgs("OK\n");
            }
            this.printMsgs();
        } catch (Exception e) {System.out.println("Error @ GETS All request: " + e);}
    }

    private void sendMsgs(String msg) {
        try {
            this.output.write(msg.getBytes());
            this.output.flush();
            System.out.println("Client says: " + msg);	
        } catch (Exception e) {System.out.println("Error @ sending message to server: " + e);}
    }

    private void printMsgs() {
        try {
            buffer = this.input.readLine();
            System.out.println("Server says: " + buffer);
        } catch (Exception e) {System.out.println("Error @ printing server messages: " + e);}
    }

    private void getFirstLargestServer(String msg) throws Exception {
        Pattern reg = Pattern.compile("([^ ])+");
        Matcher m = reg.matcher(msg);
        if(m.find()) {
            this.largestServer = m.group(0);
        }
        else {
            throw new Exception("Could not find largest server ID");
        }
    }
}
