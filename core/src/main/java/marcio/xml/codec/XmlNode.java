package marcio.xml.codec;

import java.util.LinkedList;
import java.util.List;

/**
 * Codec class
 */
public class XmlNode {

    public String context; // xpath
    public String nodeType; // g, path, circle
    public List<XmlNodeAttribute> attributes = new LinkedList<>();
    public StringBuilder text; // xml value to be parsed by batik?

    public XmlNode(String context, String nodeType, StringBuilder text) {
        this.context = context;
        this.nodeType = nodeType;
        this.text = text;
    }


    @Override
    public String toString() {
        StringBuilder attributesBuilder = new StringBuilder("[");
        for (XmlNodeAttribute xmlNodeAttribute: attributes             ) {
            attributesBuilder.append(xmlNodeAttribute).append(",");
        }
        attributesBuilder.replace(attributesBuilder.length()-1,attributesBuilder.length()-1,"]");

        return "XmlNode{" +
                "context='" + context + '\'' +
                ", nodeType='" + nodeType + '\'' +
                ", attributes=" + attributesBuilder.toString() +
                ", text=" + text +
                '}';
    }
}
