import java.net.*;
import java.io.*;

public class Client {
    String ip;
    int port;
    Socket client;
    BufferedReader input;
    DataOutputStream output;
    
    Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    Client () {
        this.ip = "localhost";
        this.port = 50000;
    }

    public void initalise() {
        try {
            this.client = new Socket("localhost", 50000);
            this.input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            this.output = new DataOutputStream(client.getOutputStream());
        } catch (Exception e) {System.out.println("Error: " + e);}       
    }

    public void auth() {
        
    }

}
