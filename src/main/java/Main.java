import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Created by DavidLANG on 22/05/2017.
 */
public class Main {


    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        Parser parser = new Parser("trace.xml");
        parser.parse();

    }

}
