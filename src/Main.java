public class Main {
    public static void main(String[] args) {
        try {
            Client client = new Client("localhost", 50000);
            client.execute();            
        } catch (Exception e) {System.out.println("Unknown error has occurred"); e.printStackTrace();}
    }
}