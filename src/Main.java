public class Main {
    public static void main(String[] args) {
        try {

            String algo = "";
            algo = "bgaq";
            Client client = new Client("localhost", 50000, algo);
            client.execute();
            
        } catch (Exception e) {System.out.println("Unknown error has occurred"); e.printStackTrace();}
    }
}