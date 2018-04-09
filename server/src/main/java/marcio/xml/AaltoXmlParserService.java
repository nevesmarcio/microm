package marcio.xml;

import com.fasterxml.aalto.AsyncByteArrayFeeder;
import com.fasterxml.aalto.AsyncXMLInputFactory;
import com.fasterxml.aalto.AsyncXMLStreamReader;
import com.fasterxml.aalto.stax.InputFactoryImpl;
import marcio.xml.codec.XmlNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

public class AaltoXmlParserService extends AbstractXmlParserService {
    private static final Logger log = LoggerFactory.getLogger(AaltoXmlParserService.class);

    private AsyncXMLInputFactory XML_INPUT_FACTORY = new InputFactoryImpl(); // sub-class of XMLStreamReader2
    private AsyncXMLStreamReader<AsyncByteArrayFeeder> parser = XML_INPUT_FACTORY.createAsyncForByteArray();

    public AaltoXmlParserService() {
    }

    @Override
    public void feedInput(byte[] data, int offset, int len) throws Exception {
        parser.getInputFeeder().feedInput(data, offset, len);
        int event_type;
        while ((event_type = parser.next()) != AsyncXMLStreamReader.EVENT_INCOMPLETE) {
            decode(event_type);
        }
    }

    @Override
    public void endOfInput() {
        parser.getInputFeeder().endOfInput();
    }

    private void decode(int type) throws XMLStreamException {
        log.trace(">>>>decode enter");

//        int type = parser.next();
        switch (type) {
            case XMLStreamConstants.START_DOCUMENT:
                log.debug("START_DOCUMENT :: {},{},{},{}", new Object[]{parser.getEncoding(), parser.getVersion(), parser.isStandalone(), parser.getCharacterEncodingScheme()});
                //out.add(new XmlDocumentStart(parser.getEncoding(), parser.getVersion(), parser.isStandalone(), parser.getCharacterEncodingScheme()));
                break;
            case XMLStreamConstants.END_DOCUMENT:
                log.debug("END_DOCUMENT :: {}", new Object[]{parser});
                //out.add(XML_DOCUMENT_END);
                break;
            case XMLStreamConstants.START_ELEMENT:
                log.debug("START_ELEMENT :: {},{},{}", new Object[]{parser.getLocalName(), parser.getName().getNamespaceURI(), parser.getPrefix()});
                XmlNode nodeToAdd = new XmlNode(getCurrentContext(), parser.getLocalName(), new StringBuilder());
                xmlElements.push(nodeToAdd);

                for (int x = 0; x < parser.getAttributeCount(); x++) {
                    log.debug(">>${},{},{},{},{}", parser.getAttributeType(x),
                            parser.getAttributeLocalName(x), parser.getAttributePrefix(x),
                            parser.getAttributeNamespace(x), parser.getAttributeValue(x));
                    nodeToAdd.attributes.put(parser.getAttributeLocalName(x), parser.getAttributeValue(x));
                }
                for (int x = 0; x < parser.getNamespaceCount(); x++) {
                    log.debug(">>$${},{}", parser.getNamespacePrefix(x),
                            parser.getNamespaceURI(x));
                }

                log.debug(">>>>{}", nodeToAdd.toString());
                if (xmlNodeEmitter != null) xmlNodeEmitter.emit(nodeToAdd);

                //XmlElementStart elementStart = new XmlElementStart(parser.getLocalName(), parser.getName().getNamespaceURI(), parser.getPrefix());
                //for (int x = 0; x < parser.getAttributeCount(); x++) {
                //    XmlAttribute attribute = new XmlAttribute(parser.getAttributeType(x),
                //            parser.getAttributeLocalName(x), parser.getAttributePrefix(x),
                //            parser.getAttributeNamespace(x), parser.getAttributeValue(x));
                //    elementStart.attributes().add(attribute);
                //}
                //for (int x = 0; x < parser.getNamespaceCount(); x++) {
                //    XmlNamespace namespace = new XmlNamespace(parser.getNamespacePrefix(x),
                //            parser.getNamespaceURI(x));
                //    elementStart.namespaces().add(namespace);
                //}
                //out.add(elementStart);
                break;
            case XMLStreamConstants.END_ELEMENT:
                XmlNode xmlDataNode = xmlElements.pop();

                log.debug("END_ELEMENT :: {},{},{} | CONTENT :: {}", new Object[]{
                        parser.getLocalName(),
                        parser.getName().getNamespaceURI(),
                        parser.getPrefix(),
                        xmlDataNode.text.toString()
                });

                log.debug("<<<<{}", xmlDataNode.toString());

                //XmlElementEnd elementEnd = new XmlElementEnd(parser.getLocalName(),
                //        parser.getName().getNamespaceURI(), parser.getPrefix());
                //for (int x = 0; x < parser.getNamespaceCount(); x++) {
                //    XmlNamespace namespace = new XmlNamespace(parser.getNamespacePrefix(x),
                //            parser.getNamespaceURI(x));
                //    elementEnd.namespaces().add(namespace);
                //}
                //out.add(elementEnd);

                break;
            case XMLStreamConstants.PROCESSING_INSTRUCTION:
                log.debug("PROCESSING_INSTRUCTION");
                //out.add(new XmlProcessingInstruction(parser.getPIData(), parser.getPITarget()));
                break;
            case XMLStreamConstants.CHARACTERS:
                log.debug("CHARACTERS :: {}", new Object[]{parser.getText()});
                xmlElements.peek().text.append(parser.getText());
                //out.add(new XmlCharacters(parser.getText()));
                break;
            case XMLStreamConstants.COMMENT:
                log.debug("COMMENT");
                //out.add(new XmlComment(parser.getText()));
                break;
            case XMLStreamConstants.SPACE:
                log.debug("SPACE");
                //out.add(new XmlSpace(parser.getText()));
                break;
            case XMLStreamConstants.ENTITY_REFERENCE:
                log.debug("ENTITY_REFERENCE");
                //out.add(new XmlEntityReference(parser.getLocalName(), parser.getText()));
                break;
            case XMLStreamConstants.DTD:
                log.debug("DTD");
                //out.add(new XmlDTD(parser.getText()));
                break;
            case XMLStreamConstants.CDATA:
                log.debug("CDATA");
                //out.add(new XmlCdata(parser.getText()));
                break;
            case AsyncXMLStreamReader.EVENT_INCOMPLETE:
                log.debug("EVENT_INCOMPLETE");
                break;
            default:
                log.debug("UNEXPECTED TYPE FOUND! = '{}'.", type);
                break;
        }
        log.trace("<<<<<decode exit");
    }

    @Override
    protected void finalize() throws Throwable {
        log.warn("GARGAGE COLLECTED this!");
        super.finalize();
    }
}
