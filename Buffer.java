import java.io.BufferedReader;

public class Buffer {
    private String buffer;
    BufferedReader input;

    Buffer(BufferedReader input) {
        this.input = input;
        this.buffer = "";
    }

    public String get() {
        return buffer;
    }

    public void update(){
        try {
            buffer = input.readLine();
        }
        catch (Exception e) {System.out.println("Could not update buffer");}
    }

    public boolean contains(String msg) {
        return buffer.contains(msg);
    }

    public boolean equals(String msg) {
        return buffer.equals(msg);
    }
}
