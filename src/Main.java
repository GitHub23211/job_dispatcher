public class Main {
    public static void main(String[] args) {
        try {
                String algo = "";
                String parse = "";

                if(args.length <= 1) {
                    algo = "LRR";
                    parse = "XML";
                }
                else {
                    algo = args[0];
                    parse = args[1];
                }

                Client client = new Client("localhost", 50000, algo, parse);
                client.execute();
                
        } catch (Exception e) {System.out.println("Usage: \narg 1 - LRR or ATL \n arg 2 - XML or GETS "); e.printStackTrace();}
    }
}