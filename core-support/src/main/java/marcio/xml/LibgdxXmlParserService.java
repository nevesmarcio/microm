package marcio.xml;

import com.badlogic.gdx.utils.XmlReader;
import marcio.xml.codec.XmlNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class LibgdxXmlParserService extends AbstractXmlParserService {
    private static final Logger log = LoggerFactory.getLogger(LibgdxXmlParserService.class);

    private EventEmittingXmlParser parser = new EventEmittingXmlParser();

    public LibgdxXmlParserService() {
    }

    @Override
    public void feedInput(byte[] data, int offset, int len) throws Exception {
        char[] cbuf = new String(data, Charset.defaultCharset()).toCharArray();
        parser.parse(cbuf, offset, len);
    }

    @Override
    public void endOfInput() {

    }

    private class EventEmittingXmlParser extends XmlReader {

        @Override
        protected void open(String name) {
            super.open(name);
            XmlNode nodeToAdd = new XmlNode(getCurrentContext(), name, new StringBuilder());
            xmlElements.push(nodeToAdd);
        }

        @Override
        protected void attribute(String name, String value) {
            super.attribute(name, value);
            xmlElements.peek().attributes.put(name,value);
        }

        @Override
        protected String entity(String name) {
            return super.entity(name);
        }

        @Override
        protected void text(String text) {
            super.text(text);
            xmlElements.peek().text.append(text);
        }

        @Override
        protected void close() {
            super.close();
            if (xmlNodeEmitter != null) xmlNodeEmitter.emit(xmlElements.pop());
        }
    }
}


