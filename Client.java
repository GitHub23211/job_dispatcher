import java.net.*;
import java.io.*;


public class Client {
    private String ip;
    private int port;
    private Socket client;
    private BufferedReader input;
    private DataOutputStream output;
    private Buffer buffer;
    private Message msg;
    private Parser parser;
    private Scheduler schedule;
    
    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
        initalise();
    }

    private void initalise() {
        try {
            client = new Socket(ip, port);
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            output = new DataOutputStream(client.getOutputStream());
            msg = new Message(output, buffer);
            buffer = new Buffer(input);
            parser = new Parser();
        } catch (Exception e) {System.out.println("Error @ initalisaiton: " + e);}       
    }

    public void auth() {
        try {
            String[] msgs = {"HELO", "AUTH user", "REDY"};
            for(int i = 0; i < msgs.length; i++) {
                msg.send(msgs[i]);
            }
        } catch (Exception e) {System.out.println("Error @ authentication: " + e);}
    }

    public void close() {
        try {
            msg.send("QUIT");
            if(buffer.equals("QUIT")) {
                client.close();
            }
        } catch (Exception e) {System.out.println("COULD NOT CLOSE CLIENT GRACEFULLY");}

    }
}
