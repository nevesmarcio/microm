package marcio.xml.codec;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Codec class
 */
public class XmlNode {

    public String context; // xpath
    public String nodeType; // g, path, circle
    public HashMap<String, String> attributes = new HashMap<>();
    public StringBuilder text; // xml value to be parsed by batik?

    public XmlNode(String context, String nodeType, StringBuilder text) {
        this.context = context;
        this.nodeType = nodeType;
        this.text = text;
    }


    @Override
    public String toString() {
        StringBuilder attributesBuilder = new StringBuilder("[");
        for (Map.Entry<String, String> xmlNodeAttribute: attributes.entrySet()) {
            attributesBuilder.append("XmlNodeAttribute{")
                    .append("name='")
                    .append(xmlNodeAttribute.getKey())
                    .append('\'')
                    .append(", value='")
                    .append(xmlNodeAttribute.getValue())
                    .append('\'')
                    .append('}')
                    .append(",");
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