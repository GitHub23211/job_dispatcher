public class Main {
    public static void main(String[] args) {
        Client client = new Client("localhost", 50000);
        client.execute();
    }
}