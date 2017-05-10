package marcio.xml;

import com.fasterxml.aalto.AsyncByteArrayFeeder;
import com.fasterxml.aalto.AsyncXMLInputFactory;
import com.fasterxml.aalto.AsyncXMLStreamReader;
import com.fasterxml.aalto.stax.InputFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Marcio on 28/02/2017.
 * link: https://github.com/FasterXML/aalto-xml/wiki/Code-sample:-Async-parsing
 *
 */
public class AsyncXmlService {
    private static final Logger log = LoggerFactory.getLogger(AsyncXmlService.class);

    AsyncXMLInputFactory XML_INPUT_FACTORY = new InputFactoryImpl(); // sub-class of XMLStreamReader2
    AsyncXMLStreamReader<AsyncByteArrayFeeder> streamReader = XML_INPUT_FACTORY.createAsyncForByteArray();
    final AsyncByteArrayFeeder streamFeeder = streamReader.getInputFeeder();

    public AsyncXmlService() {

    }


    // Method delegation for feeder
    public void feedInput(byte[] data, int offset, int len) throws XMLStreamException {
        streamFeeder.feedInput(data, offset, len);
        try {
            decode(streamReader, streamFeeder, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean needMoreInput() {
        return streamFeeder.needMoreInput();
    }

    public void endOfInput() {
        streamFeeder.endOfInput();
    }



    LinkedHashMap<String, StringBuilder> decoderBuffer = new LinkedHashMap<>();
    private void decode(AsyncXMLStreamReader<AsyncByteArrayFeeder> streamReader, AsyncByteArrayFeeder streamFeeder, List<Object> out) throws Exception {
        log.trace(">>>>decode enter");
        while (!streamFeeder.needMoreInput() && streamReader.hasNext()) {
//            Thread.sleep(2000);
            int type = streamReader.next();
            switch (type) {
                case XMLStreamConstants.START_DOCUMENT:
                    log.debug("START_DOCUMENT :: {},{},{},{}", new Object[]{streamReader.getEncoding(), streamReader.getVersion(), streamReader.isStandalone(), streamReader.getCharacterEncodingScheme()});
//                    out.add(new XmlDocumentStart(streamReader.getEncoding(), streamReader.getVersion(), streamReader.isStandalone(), streamReader.getCharacterEncodingScheme()));
                    break;
                case XMLStreamConstants.END_DOCUMENT:
                    log.debug("END_DOCUMENT :: {}", new Object[]{streamReader});
//                    out.add(XML_DOCUMENT_END);
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    log.debug("START_ELEMENT :: {},{},{}", new Object[]{streamReader.getLocalName(), streamReader.getName().getNamespaceURI(), streamReader.getPrefix()});
                    for (int x = 0; x < streamReader.getAttributeCount(); x++) {
                        log.debug("${},{},{},{},{}", streamReader.getAttributeType(x),
                                streamReader.getAttributeLocalName(x), streamReader.getAttributePrefix(x),
                                streamReader.getAttributeNamespace(x), streamReader.getAttributeValue(x));
                    }
                    for (int x = 0; x < streamReader.getNamespaceCount(); x++) {
                        log.debug("${},{}", streamReader.getNamespacePrefix(x),
                                streamReader.getNamespaceURI(x));
                    }
                    decoderBuffer.put(streamReader.getLocalName(), new StringBuilder());

//                    XmlElementStart elementStart = new XmlElementStart(streamReader.getLocalName(), streamReader.getName().getNamespaceURI(), streamReader.getPrefix());
//                    for (int x = 0; x < streamReader.getAttributeCount(); x++) {
//                        XmlAttribute attribute = new XmlAttribute(streamReader.getAttributeType(x),
//                                streamReader.getAttributeLocalName(x), streamReader.getAttributePrefix(x),
//                                streamReader.getAttributeNamespace(x), streamReader.getAttributeValue(x));
//                        elementStart.attributes().add(attribute);
//                    }
//                    for (int x = 0; x < streamReader.getNamespaceCount(); x++) {
//                        XmlNamespace namespace = new XmlNamespace(streamReader.getNamespacePrefix(x),
//                                streamReader.getNamespaceURI(x));
//                        elementStart.namespaces().add(namespace);
//                    }
//                    out.add(elementStart);
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    log.debug("END_ELEMENT :: {},{},{}", new Object[]{streamReader.getLocalName(), streamReader.getName().getNamespaceURI(), streamReader.getPrefix()});
//                    XmlElementEnd elementEnd = new XmlElementEnd(streamReader.getLocalName(),
//                            streamReader.getName().getNamespaceURI(), streamReader.getPrefix());
//                    for (int x = 0; x < streamReader.getNamespaceCount(); x++) {
//                        XmlNamespace namespace = new XmlNamespace(streamReader.getNamespacePrefix(x),
//                                streamReader.getNamespaceURI(x));
//                        elementEnd.namespaces().add(namespace);
//                    }
//                    out.add(elementEnd);
                    log.debug("CONTENT={}", decoderBuffer.get(streamReader.getLocalName()).toString());
                    decoderBuffer.remove(streamReader.getLocalName());

                    break;
                case XMLStreamConstants.PROCESSING_INSTRUCTION:
                    log.debug("PROCESSING_INSTRUCTION");
//                    out.add(new XmlProcessingInstruction(streamReader.getPIData(), streamReader.getPITarget()));
                    break;
                case XMLStreamConstants.CHARACTERS:
                    log.debug("CHARACTERS :: {}", new Object[]{streamReader.getText()});
                    ((StringBuilder) decoderBuffer.values().toArray()[decoderBuffer.size()-1]).append(streamReader.getText());
//                    out.add(new XmlCharacters(streamReader.getText()));
                    break;
                case XMLStreamConstants.COMMENT:
                    log.debug("COMMENT");
//                    out.add(new XmlComment(streamReader.getText()));
                    break;
                case XMLStreamConstants.SPACE:
                    log.debug("SPACE");
//                    out.add(new XmlSpace(streamReader.getText()));
                    break;
                case XMLStreamConstants.ENTITY_REFERENCE:
                    log.debug("ENTITY_REFERENCE");
//                    out.add(new XmlEntityReference(streamReader.getLocalName(), streamReader.getText()));
                    break;
                case XMLStreamConstants.DTD:
                    log.debug("DTD");
//                    out.add(new XmlDTD(streamReader.getText()));
                    break;
                case XMLStreamConstants.CDATA:
                    log.debug("CDATA");
//                    out.add(new XmlCdata(streamReader.getText()));
                    break;
                case AsyncXMLStreamReader.EVENT_INCOMPLETE:
                    log.debug("EVENT_INCOMPLETE");
                break;
                default:
                    log.debug("UNEXPECTED TYPE FOUND! = '{}'.", type);
                    break;
            }
        }
        log.trace("<<<<<decode exit");
    }
}
