public class Main {
    public static void main(String[] args) {
        String algo = "";
        if(args.length <= 0) {
            algo = "LRR";
        }
        else {
            algo = args[0];
        }
        Client client = new Client("localhost", 50000, algo);
        client.execute();
    }
}