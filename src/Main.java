public class Main {
    public static void main(String[] args) {
        try {
            String algo = "";
            String parse = "";

            if(args.length < 1) {
                System.out.println("Usage: Main lga | bga \nlga - Lazy Gets Available \nbga - Best Gets Available");
            }
            else {
                algo = args[0].toLowerCase();
                parse = "XML";
                Client client = new Client("localhost", 50000, algo, parse);
                client.execute();
            }


                
        } catch (Exception e) {System.out.println("Unknown error has occurred"); e.printStackTrace();}
    }
}