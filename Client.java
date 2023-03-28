import java.net.*;
import java.io.*;

public class Client {
    String ip;
    int port;
    Socket client;
    BufferedReader input;
    DataOutputStream output;
    String buffer;
    
    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.buffer = "";
        this.initalise();
    }

    private void initalise() {
        try {
            this.client = new Socket("localhost", 50000);
            this.input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            this.output = new DataOutputStream(client.getOutputStream());
        } catch (Exception e) {System.out.println("Error @ initalisaiton: " + e);}       
    }

    public void auth() {
        try {
            String[] msgs = {"HELO\n", "AUTH 450203083\n"};
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
