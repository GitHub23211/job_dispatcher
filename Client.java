import java.net.*;
import java.io.*;

public class Client {
    private String ip;
    private int port;
    private Socket client;
    private BufferedReader input;
    private DataOutputStream output;
    private String buffer;
    
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
            String[] msgs = {"HELO\n", "AUTH user\n"};
            for(int i = 0; i < msgs.length; i++) {
                this.output.write(msgs[i].getBytes());
                this.output.flush();
                System.out.println("Client says: " + msgs[i]);	
                buffer = input.readLine();
                System.out.println("Server says: " + buffer);
            }
        } catch (Exception e) {System.out.println("Error @ authentication: " + e);}
    }

}
