package marcio.xml;

import com.fasterxml.aalto.AsyncByteArrayFeeder;
import com.fasterxml.aalto.AsyncXMLInputFactory;
import com.fasterxml.aalto.AsyncXMLStreamReader;
import com.fasterxml.aalto.stax.InputFactoryImpl;
import io.reactivex.*;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import marcio.xml.codec.XmlNode;
import marcio.xml.codec.XmlNodeAttribute;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import java.util.Stack;

/**
 * Created by Marcio on 28/02/2017.
 * link: https://github.com/FasterXML/aalto-xml/wiki/Code-sample:-Async-parsing
 * <p>
 * link: https://medium.com/yammer-engineering/converting-callback-async-calls-to-rxjava-ebc68bde5831
 */
public class AsyncXmlService implements FlowableTransformer<String, XmlNode> {
    private static final Logger log = LoggerFactory.getLogger(AsyncXmlService.class);

    private AsyncXMLInputFactory XML_INPUT_FACTORY = new InputFactoryImpl(); // sub-class of XMLStreamReader2
    private AsyncXMLStreamReader<AsyncByteArrayFeeder> streamReader = XML_INPUT_FACTORY.createAsyncForByteArray();
    private final AsyncByteArrayFeeder streamFeeder = streamReader.getInputFeeder();

    private XmlNodeHandler xmlNodeHandler;

    public AsyncXmlService(XmlNodeHandler xmlNodeHandler) {
        this.xmlNodeHandler = xmlNodeHandler;
        if (xmlNodeHandler == null) {
            this.xmlNodeHandler = new XmlNodeHandler() {
                @Override
                public void handle(XmlNode xmlNode) {
                    System.out.println("Emitting: " + xmlNode.toString());
                }
            };
        }
    }

    //region interface1: manual methods
    //---------------------------------------------------------------------------------------
    public void feedInput(byte[] data, int offset, int len) throws XMLStreamException {
        log.info("feedInput");
        streamFeeder.feedInput(data, offset, len);
        while (!streamFeeder.needMoreInput() && streamReader.hasNext()) { // this will guarantee several runs that empty the buffer
            log.info("decodeInput");
            decode();
        }
    }

    public void endOfInput() {
        streamFeeder.endOfInput();
    }
    //---------------------------------------------------------------------------------------
    //endregion interface1: manual methods


    //region interface2: reactivex interface - observable
    //---------------------------------------------------------------------------------------


    @Override
    public Publisher<XmlNode> apply(final Flowable<String> upstream) {
        log.info("transforming!!");

        FlowableOnSubscribe<XmlNode> flowableOnSubscribe = new FlowableOnSubscribe<XmlNode>() {
            @Override
            public void subscribe(final FlowableEmitter<XmlNode> e) throws Exception {
                // on subscription register the xmlnodeHandler callback
                AsyncXmlService.this.xmlNodeHandler = new XmlNodeHandler() {
                    @Override
                    public void handle(XmlNode xmlNode) {
                        e.onNext(xmlNode);
                    }
                };
                // on subscription register to the upstream events to provide stimuli to asyncxmlservice

                upstream.subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        AsyncXmlService.this.feedInput(s.getBytes(), 0, s.length());
                    }
                });

            }
        };
        Flowable<XmlNode> output = Flowable.create(flowableOnSubscribe, BackpressureStrategy.ERROR);
        return output;
    }


    //---------------------------------------------------------------------------------------
    //endregion interface2: reactivex interface - observable

    //region context maintenance
    //---------------------------------------------------------------------------------------
    private Stack<XmlNode> xmlElements = new Stack<>();

    private String getCurrentContext() {
        StringBuilder sb = new StringBuilder();
        for (XmlNode dn : xmlElements) {
            sb.append("/");
            sb.append(dn.nodeType);
        }
        if (!(sb.length() > 0)) {
            sb.append("/");
        }
        return sb.toString();
    }
    //---------------------------------------------------------------------------------------
    //endregion context maintenance

    private void decode() throws XMLStreamException {
        log.trace(">>>>decode enter");

        int type = streamReader.next();
        switch (type) {
            case XMLStreamConstants.START_DOCUMENT:
                log.debug("START_DOCUMENT :: {},{},{},{}", new Object[]{streamReader.getEncoding(), streamReader.getVersion(), streamReader.isStandalone(), streamReader.getCharacterEncodingScheme()});
                //out.add(new XmlDocumentStart(streamReader.getEncoding(), streamReader.getVersion(), streamReader.isStandalone(), streamReader.getCharacterEncodingScheme()));
                break;
            case XMLStreamConstants.END_DOCUMENT:
                log.debug("END_DOCUMENT :: {}", new Object[]{streamReader});
                //out.add(XML_DOCUMENT_END);
                break;
            case XMLStreamConstants.START_ELEMENT:
                log.debug("START_ELEMENT :: {},{},{}", new Object[]{streamReader.getLocalName(), streamReader.getName().getNamespaceURI(), streamReader.getPrefix()});
                XmlNode nodeToAdd = new XmlNode(getCurrentContext(), streamReader.getLocalName(), new StringBuilder());
                xmlElements.push(nodeToAdd);

                for (int x = 0; x < streamReader.getAttributeCount(); x++) {
                    log.debug(">>${},{},{},{},{}", streamReader.getAttributeType(x),
                            streamReader.getAttributeLocalName(x), streamReader.getAttributePrefix(x),
                            streamReader.getAttributeNamespace(x), streamReader.getAttributeValue(x));
                    nodeToAdd.attributes.add(new XmlNodeAttribute(streamReader.getAttributeLocalName(x), streamReader.getAttributeValue(x)));
                }
                for (int x = 0; x < streamReader.getNamespaceCount(); x++) {
                    log.debug(">>$${},{}", streamReader.getNamespacePrefix(x),
                            streamReader.getNamespaceURI(x));
                }

                log.info(">>>>{}", nodeToAdd.toString());
                xmlNodeHandler.handle(nodeToAdd);

                //XmlElementStart elementStart = new XmlElementStart(streamReader.getLocalName(), streamReader.getName().getNamespaceURI(), streamReader.getPrefix());
                //for (int x = 0; x < streamReader.getAttributeCount(); x++) {
                //    XmlAttribute attribute = new XmlAttribute(streamReader.getAttributeType(x),
                //            streamReader.getAttributeLocalName(x), streamReader.getAttributePrefix(x),
                //            streamReader.getAttributeNamespace(x), streamReader.getAttributeValue(x));
                //    elementStart.attributes().add(attribute);
                //}
                //for (int x = 0; x < streamReader.getNamespaceCount(); x++) {
                //    XmlNamespace namespace = new XmlNamespace(streamReader.getNamespacePrefix(x),
                //            streamReader.getNamespaceURI(x));
                //    elementStart.namespaces().add(namespace);
                //}
                //out.add(elementStart);
                break;
            case XMLStreamConstants.END_ELEMENT:
                XmlNode xmlDataNode = xmlElements.pop();

                log.debug("END_ELEMENT :: {},{},{} | CONTENT :: {}", new Object[]{
                        streamReader.getLocalName(),
                        streamReader.getName().getNamespaceURI(),
                        streamReader.getPrefix(),
                        xmlDataNode.text.toString()
                });

                log.info("<<<<{}", xmlDataNode.toString());

                //XmlElementEnd elementEnd = new XmlElementEnd(streamReader.getLocalName(),
                //        streamReader.getName().getNamespaceURI(), streamReader.getPrefix());
                //for (int x = 0; x < streamReader.getNamespaceCount(); x++) {
                //    XmlNamespace namespace = new XmlNamespace(streamReader.getNamespacePrefix(x),
                //            streamReader.getNamespaceURI(x));
                //    elementEnd.namespaces().add(namespace);
                //}
                //out.add(elementEnd);

                break;
            case XMLStreamConstants.PROCESSING_INSTRUCTION:
                log.debug("PROCESSING_INSTRUCTION");
                //out.add(new XmlProcessingInstruction(streamReader.getPIData(), streamReader.getPITarget()));
                break;
            case XMLStreamConstants.CHARACTERS:
                log.debug("CHARACTERS :: {}", new Object[]{streamReader.getText()});
                xmlElements.peek().text.append(streamReader.getText());
                //out.add(new XmlCharacters(streamReader.getText()));
                break;
            case XMLStreamConstants.COMMENT:
                log.debug("COMMENT");
                //out.add(new XmlComment(streamReader.getText()));
                break;
            case XMLStreamConstants.SPACE:
                log.debug("SPACE");
                //out.add(new XmlSpace(streamReader.getText()));
                break;
            case XMLStreamConstants.ENTITY_REFERENCE:
                log.debug("ENTITY_REFERENCE");
                //out.add(new XmlEntityReference(streamReader.getLocalName(), streamReader.getText()));
                break;
            case XMLStreamConstants.DTD:
                log.debug("DTD");
                //out.add(new XmlDTD(streamReader.getText()));
                break;
            case XMLStreamConstants.CDATA:
                log.debug("CDATA");
                //out.add(new XmlCdata(streamReader.getText()));
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
        log.warn("GARGAGE COLLECTED thi!");
        super.finalize();
    }
}
