import java.net.*;
import java.io.*;

public class Client {
    private String ip;
    private int port;
    private Socket socket;
    private BufferedReader input;
    private DataOutputStream output;
    private Buffer buffer;
    private Message msg;
    private Scheduler schedule;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
        initalise();
    }

    private void initalise() {
        try {
            socket = new Socket(ip, port);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new DataOutputStream(socket.getOutputStream());
            buffer = new Buffer(input);
            msg = new Message(output, buffer);
        } catch (Exception e) {System.out.println("Error @ initalisaiton: " + e);}       
    }

    public void execute() {
        auth();
        run();
        if(!socket.isClosed()) {
            close();
        }
    }

    public void auth() {
        try {
            String[] msgs = {"HELO", "AUTH " + System.getProperty("user.name"), "REDY"};
            for(int i = 0; i < msgs.length; i++) {
                msg.send(msgs[i]);
            }
        } catch (Exception e) {System.out.println("Error @ authentication: " + e);}
    }

    public void run() {
        schedule = new GetsAvailExhaust(buffer, msg);
        schedule.execute();
    }

    public void close() {
        try {
            msg.send("QUIT");
            if(buffer.equals("QUIT")) {
                socket.close();
            }
        } catch (Exception e) {System.out.println("COULD NOT CLOSE CLIENT GRACEFULLY");}

    }
}
