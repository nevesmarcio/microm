package marcio.batik;

import marcio.nio.AsyncIOChunked;
import marcio.nio.ChunkReadHandler;
import marcio.xml.AsyncXmlParserService;
import marcio.xml.XmlNodeHandler;
import marcio.xml.codec.XmlNode;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.nio.file.Paths;


public class LevelLoaderTest {

    private static final Logger log = LoggerFactory.getLogger(LevelLoaderTest.class);

    final private AsyncIOChunked asyncIOChunked = new AsyncIOChunked();
    final private AsyncXmlParserService asyncXmlService = new AsyncXmlParserService();
    final private SvgService svgService = new SvgService(new IAppendable() {
        @Override
        public void append() {
            log.info("appending...");
        }
    });



    private String p;

    @Before
    public void setUp() throws Exception {
        p = "src/test/resources/one-shape-only.svg";
        asyncXmlService.setXmlNodeHandler(new XmlNodeHandler() {
            @Override
            public void handle(XmlNode xmlNode) {
                System.out.println("\t\t\t>>>Emitting: " + xmlNode.toString());
                svgService.feedInput(xmlNode);
            }
        });
    }

    @Test
    public void testRead() throws IOException, InterruptedException, XMLStreamException {

        log.info("start test:{}", this.getClass().getSimpleName());
        log.debug("Working dir: {}", Paths.get(".").toAbsolutePath().toString());

        asyncIOChunked.asyncRead(p, new ChunkReadHandler() {
            @Override
            public void handle(String chunk) {
                try {
                    log.info("chunk read - feeding parser>>>");
                    asyncXmlService.feedInput(chunk.getBytes(), 0, chunk.getBytes().length);
                } catch (XMLStreamException e) {
                    log.error("Couldn't parse chunk!");
                }
            }
        });

        Thread.sleep(1000);
        log.info("end test:{}", this.getClass().getSimpleName());

    }

}