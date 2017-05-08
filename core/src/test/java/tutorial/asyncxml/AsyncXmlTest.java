package tutorial.asyncxml;

import com.fasterxml.aalto.AsyncByteArrayFeeder;
import com.fasterxml.aalto.AsyncXMLInputFactory;
import com.fasterxml.aalto.AsyncXMLStreamReader;
import com.fasterxml.aalto.stax.InputFactoryImpl;
import helper.GameTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import java.util.*;

public class AsyncXmlTest extends GameTest {
    private static final Logger log = LoggerFactory.getLogger(AsyncXmlTest.class.getSimpleName());

    @Test
    public void testAsyncXMLRead() {
        log.debug("testAsyncXMLRead");
//        String p = Gdx.files.internal("employee.xml").path();


        AsyncXMLInputFactory XML_INPUT_FACTORY = new InputFactoryImpl(); // sub-class of XMLStreamReader2
        AsyncXMLStreamReader<AsyncByteArrayFeeder> streamReader = XML_INPUT_FACTORY.createAsyncForByteArray();
        final AsyncByteArrayFeeder streamFeeder = streamReader.getInputFeeder();

        Thread generator = new Thread(new Runnable() {
            @Override
            public void run() {
                long timeA;

                try {
//                    Thread.sleep(3000);
                    log.debug("part1");
                    String p1 = "<employee style=\"uber\">\n" +
                            "    <id>1</id>\n" +
                            "    <na";
                    timeA = System.currentTimeMillis();
                    while (!streamFeeder.needMoreInput()) {
                        System.out.print(".");
                        Thread.sleep(100);
                    }
                    streamFeeder.feedInput(p1.getBytes(), 0, p1.getBytes().length);
                    log.info("waited: {}ms", new Object[]{System.currentTimeMillis() - timeA});

//                    Thread.sleep(3000);
                    log.debug("part2");
                    String p2 = "me>Alba</name>    <salary>10";
                    timeA = System.currentTimeMillis();
                    while (!streamFeeder.needMoreInput()) {
                        System.out.print(".");
                        Thread.sleep(100);
                    }
                    streamFeeder.feedInput(p2.getBytes(), 0, p2.getBytes().length);
                    log.info("waited: {}ms", new Object[]{System.currentTimeMillis() - timeA});

//                    Thread.sleep(3000);
                    log.debug("part3");
                    String p3 = "0</salary>\n" +
                            "<traits eyes=\"brown\" hei";
                    timeA = System.currentTimeMillis();
                    while (!streamFeeder.needMoreInput()) {
                        System.out.print(".");
                        Thread.sleep(100);
                    }
                    streamFeeder.feedInput(p3.getBytes(), 0, p3.getBytes().length);
                    log.info("waited: {}ms", new Object[]{System.currentTimeMillis() - timeA});

//                    Thread.sleep(3000);
                    log.debug("part4");
                    String p4 = "ght=\"177\"/>\n" +
                            "<other/>\n" +
                            "</employee>\n";
                    timeA = System.currentTimeMillis();
                    while (!streamFeeder.needMoreInput()) {
                        System.out.print(".");
                        Thread.sleep(100);
                    }
                    streamFeeder.feedInput(p4.getBytes(), 0, p4.getBytes().length);
                    log.info("waited: {}ms", new Object[]{System.currentTimeMillis() - timeA});

//                    Thread.sleep(5000);
                    log.debug("part5");
                    timeA = System.currentTimeMillis();
                    while (!streamFeeder.needMoreInput()) {
                        System.out.print(".");
                        Thread.sleep(100);
                    }
                    streamFeeder.endOfInput();
                    log.info("waited: {}ms", new Object[]{System.currentTimeMillis() - timeA});


                } catch (XMLStreamException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        generator.setDaemon(true);
        generator.start();

        List<Object> objectList = new ArrayList<>();
        while (true) {
            log.debug("*");
            try {
                decode(streamReader, streamFeeder, objectList);
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

//        Thread.sleep(5000);

    }

    LinkedHashMap<String, StringBuilder> tempStore = new LinkedHashMap<>();
    public void decode(AsyncXMLStreamReader<AsyncByteArrayFeeder> streamReader, AsyncByteArrayFeeder streamFeeder, List<Object> out) throws Exception {
        log.info("decode enter");
        while (!streamFeeder.needMoreInput() && streamReader.hasNext()) {
//            Thread.sleep(2000);
            int type = streamReader.next();
            switch (type) {
                case XMLStreamConstants.START_DOCUMENT:
                    log.debug("START_DOCUMENT: " + streamReader.toString());
//                    out.add(new XmlDocumentStart(streamReader.getEncoding(), streamReader.getVersion(), streamReader.isStandalone(), streamReader.getCharacterEncodingScheme()));
                    break;
                case XMLStreamConstants.END_DOCUMENT:
                    log.debug("END_DOCUMENT: " + streamReader.toString());
//                    out.add(XML_DOCUMENT_END);
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    log.debug("START_ELEMENT: " + streamReader.getName());
                    log.info(">>{},{},{}", streamReader.getLocalName(), streamReader.getName().getNamespaceURI(), streamReader.getPrefix());
                    for (int x = 0; x < streamReader.getAttributeCount(); x++) {
                        log.info(">>{},{},{},{},{}", streamReader.getAttributeType(x),
                                streamReader.getAttributeLocalName(x), streamReader.getAttributePrefix(x),
                                streamReader.getAttributeNamespace(x), streamReader.getAttributeValue(x));
                    }
                    for (int x = 0; x < streamReader.getNamespaceCount(); x++) {
                        log.info(">>{},{}", streamReader.getNamespacePrefix(x),
                                streamReader.getNamespaceURI(x));
                    }
                    tempStore.put(streamReader.getLocalName(), new StringBuilder());

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
                    log.debug("END_ELEMENT: " + streamReader.getName());
//                    XmlElementEnd elementEnd = new XmlElementEnd(streamReader.getLocalName(),
//                            streamReader.getName().getNamespaceURI(), streamReader.getPrefix());
//                    for (int x = 0; x < streamReader.getNamespaceCount(); x++) {
//                        XmlNamespace namespace = new XmlNamespace(streamReader.getNamespacePrefix(x),
//                                streamReader.getNamespaceURI(x));
//                        elementEnd.namespaces().add(namespace);
//                    }
//                    out.add(elementEnd);
                    log.info("content={}", tempStore.get(streamReader.getLocalName()).toString());
                    tempStore.remove(streamReader.getLocalName());

                    break;
                case XMLStreamConstants.PROCESSING_INSTRUCTION:
                    log.debug("PROCESSING_INSTRUCTION" + streamReader.toString());
//                    out.add(new XmlProcessingInstruction(streamReader.getPIData(), streamReader.getPITarget()));
                    break;
                case XMLStreamConstants.CHARACTERS:
                    log.debug("CHARACTERS: " + streamReader.getText());
                    log.info(">>{},{}", streamReader.getText());
                    ((StringBuilder)tempStore.values().toArray()[tempStore.size()-1]).append(streamReader.getText());
//                    out.add(new XmlCharacters(streamReader.getText()));
                    break;
                case XMLStreamConstants.COMMENT:
                    log.debug("COMMENT" + streamReader.toString());
//                    out.add(new XmlComment(streamReader.getText()));
                    break;
                case XMLStreamConstants.SPACE:
                    log.debug("SPACE" + streamReader.toString());
//                    out.add(new XmlSpace(streamReader.getText()));
                    break;
                case XMLStreamConstants.ENTITY_REFERENCE:
                    log.debug("ENTITY_REFERENCE" + streamReader.toString());
//                    out.add(new XmlEntityReference(streamReader.getLocalName(), streamReader.getText()));
                    break;
                case XMLStreamConstants.DTD:
                    log.debug("DTD" + streamReader.toString());
//                    out.add(new XmlDTD(streamReader.getText()));
                    break;
                case XMLStreamConstants.CDATA:
                    log.debug("CDATA" + streamReader.toString());
//                    out.add(new XmlCdata(streamReader.getText()));
                    break;
            }
        }
    }
}