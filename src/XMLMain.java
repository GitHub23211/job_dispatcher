import java.io.File;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.XMLReader;

import java.util.ArrayList; 

public class XMLMain extends Parser {

    public ArrayList<Server> servers;

    @Override
    public void parse() {
        try {
            servers = new ArrayList<Server>();
            String filename = "./ds-system.xml";
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);
            SAXParser parser = spf.newSAXParser();
            XMLReader xmlReader =  parser.getXMLReader();
            xmlReader.setContentHandler(new XMLParser(servers));
            xmlReader.parse(getFileURL(filename));
            getLargestServer();
        } catch (Exception e) {System.err.println("Error parsing XML: " + e);}

    }

    private void getLargestServer() {
        int maxCores = 0;
        for(Server s : servers) {
            int sCores = s.getCores();
            if(sCores > maxCores) {
                maxCores = sCores;
                max = Integer.parseInt(s.getId());
                name = s.getName();
            }
        }
    }

    private static String getFileURL(String filename) {
        String path = new File(filename).getAbsolutePath();
        if (File.separatorChar != '/') {
            path = path.replace(File.separatorChar, '/');
        }
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return "file:" + path;
    }
}
