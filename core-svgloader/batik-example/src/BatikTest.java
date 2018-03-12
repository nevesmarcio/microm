import helper.GameTest;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import java.io.IOException;
import java.net.URISyntaxException;

public class BatikTest extends GameTest {
    private static final Logger log = LoggerFactory.getLogger(BatikTest.class.getSimpleName());

    @Test
    public void testGetFillColor() {
        try {
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
            String uri = getClass().getClassLoader().getResource("test.svg").toURI().toString();
            Document doc = f.createDocument(uri);
            log.debug(doc.toString());

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        log.debug("test");

    }

}