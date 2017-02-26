package pt.me.microm.tools.levelloader;

import com.badlogic.gdx.files.FileHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import static pt.me.microm.tools.levelloader.XmlUtil.asList;

public class SVGLoader {
	private static final String TAG = SVGLoader.class.getSimpleName();
	private static final Logger logger = LoggerFactory.getLogger(TAG);

	private DocumentBuilder documentBuilder;
    private Document document;
	private XPath xPath;
	private XPathExpression xPathExpression;


	public SVGLoader(FileHandle fileHandle) {
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = documentBuilder.parse(fileHandle.read());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        xPath = XPathFactory.newInstance().newXPath();

    }

    public NodeList searchElementByExpression(String expression) throws XPathExpressionException {
        if (logger.isDebugEnabled()) logger.debug("xPathExpression={}",new Object[]{expression});
	    xPathExpression = xPath.compile(expression);

        NodeList nodeList = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);

        for (Node node:asList(nodeList)) {
            String d = node.getAttributes().getNamedItem("d").getNodeValue();
            String style = node.getAttributes().getNamedItem("style").getNodeValue();
            if (logger.isInfoEnabled()) logger.info("d= " + d + "; style= " + style + ";");
        }
        return nodeList;
    }

}
