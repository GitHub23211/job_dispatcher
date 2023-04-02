import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.util.ArrayList; 

public class XMLParser extends DefaultHandler {

    public ArrayList<Server> servers;
    private Server server;

    XMLParser(ArrayList<Server> servers) {
        this.servers = servers;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(localName.equals("server")) {
            server = new Server();
            server.setName(attributes.getValue(0));
            server.setId(Integer.parseInt(attributes.getValue(1)));
            server.setCores(Integer.parseInt(attributes.getValue(4)));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(localName.equals("server")) {
            servers.add(server);
        }
    }
}
