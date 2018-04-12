package marcio.xml;

import marcio.xml.codec.XmlNode;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LibgdxXmlParserServiceTest {
    private static final Logger log = LoggerFactory.getLogger(LibgdxXmlParserServiceTest.class);

    @Test
    public void testLibgdxXmlParserService() {
        log.debug("testLibgdxXmlParserService");

        final LibgdxXmlParserService asyncXmlService = new LibgdxXmlParserService();
        asyncXmlService.setXmlNodeEmitter(
                new IXmlNodeEmitter() {
                    @Override
                    public void emit(XmlNode xmlNode) {
                        System.out.println("Emitting: " + xmlNode.toString());
                    }
                });

        Thread generator = new Thread(new Runnable() {
            @Override
            public void run() {
                long timeA;

                try {
//                    Thread.sleep(3000);
                    String p1 = "<employee style=\"uber\">\n" +
                            "    <id>1</id>\n" +
                            "    <other>\n" +
                            "        <coolyn value=\"true\"/>\n" +
                            "    </other>" +
                            "    </employee>";
                    timeA = System.nanoTime();
                    log.debug("chunk-1::{}", p1);
                    asyncXmlService.feedInput(p1.getBytes(), 0, p1.getBytes().length);
                    log.info("waited: {}µs", new Object[]{(System.nanoTime() - timeA) / 1000});

//                    Thread.sleep(5000);
                    timeA = System.nanoTime();
                    log.debug("chunk-5::endOfInput()");
                    asyncXmlService.endOfInput();
                    log.info("waited: {}µs", new Object[]{(System.nanoTime() - timeA) / 1000});


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        generator.setDaemon(true);
        generator.start();

        try {
            long timeA = System.nanoTime();
            log.debug("waiting to join");
            generator.join();
            log.debug("joined - took {}µs", new Object[]{(System.nanoTime() - timeA) / 1000});

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
