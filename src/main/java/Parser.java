import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DavidLANG on 22/05/2017.
 */
public class Parser {

    private File file;
    private Map<String, Integer> action_number = new HashMap<String, Integer>();
    private Map<String, Integer> action_cout = new HashMap<String, Integer>();
    private String budget;
    private String errorType;

    public Parser(String path) {
        ClassLoader classLoader = getClass().getClassLoader();
        this.file = new File(classLoader.getResource(path).getFile());
    }

    public void parse() throws ParserConfigurationException, IOException, SAXException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        final Document document= builder.parse(file);
        final Element racine = document.getDocumentElement();
        final NodeList racineNoeuds = racine.getChildNodes();

        final int nbRacineNoeuds = racineNoeuds.getLength();

        for (int i = 0; i < nbRacineNoeuds; i++) {
            Node node = racineNoeuds.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String nodeName = node.getNodeName();
                switch (nodeName) {
                    case "context":
                        parseContext(node);
                        break;
                    case "requestanswer":
                        parseRequestAnswer(node);
                        break;
                    case "parseError":
                        parseError(node);
                        break;
                }
            }
        }
        printResult();
    }

    private void printResult() {
        System.out.println("budget total : " + budget);
        for (Map.Entry<String, Integer> entry : action_cout.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            Integer number = action_number.get(key);

            System.out.println("ACTION     NOMBRE          COUT TOTAL      COUT MOYEN");
            System.out.println(key + "         " + number + "          " + value + "          " + value / number);
        }
    }

    private void parseError(Node node) {
        final Element error = (Element) node;
        final Element name = (Element) error.getElementsByTagName("name").item(0);
        this.errorType = name.getTextContent();
    }

    private void parseContext(Node node) {
        final Element context = (Element) node;
        final Element budget = (Element) context.getElementsByTagName("budget").item(0);
        this.budget = budget.getTextContent();
        System.out.println(budget.getTextContent());
    }

    private void parseRequestAnswer(Node node) {
        final Element requestAnswer = (Element) node;
        final Element request = (Element) requestAnswer.getElementsByTagName("request").item(0);
        final Element nom_action = (Element) request.getElementsByTagName("nom_action").item(0);
        final Element cost = (Element) requestAnswer.getElementsByTagName("cost").item(0);

        if (action_cout.get(nom_action.getTextContent()) == null) {
            action_cout.put(nom_action.getTextContent(), 0);
            action_number.put(nom_action.getTextContent(), 0);
        }

        Integer incrementNumber = action_number.get(nom_action.getTextContent()).intValue() + 1;
        action_number.put(nom_action.getTextContent(), incrementNumber);
        Integer currentCost =  action_cout.get(nom_action.getTextContent());
        Integer newValue = currentCost.intValue() + Integer.parseInt(cost.getTextContent());
        action_cout.put(nom_action.getTextContent(), newValue);
    }



}
