package marcio;

import marcio.batik.IAppendable;
import marcio.batik.game1.LoadedActor;
import marcio.nio.AsyncIOChunked;
import marcio.nio.ChunkReadHandler;
import marcio.transform.AffineTransformation;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class SvgLoaderTest {

    private static final Logger log = LoggerFactory.getLogger(SvgLoaderTest.class);

    private CountDownLatch expectedItemsLoaded = new CountDownLatch(7);
    private SvgLoader svgLoader = new SvgLoader(new AffineTransformation(), new IAppendable() {
        @Override
        public void append(LoadedActor loadedActor) {
            log.info("-->appending {}", loadedActor);
            expectedItemsLoaded.countDown();
        }
    });

    private String p;

    @Before
    public void setUp() throws Exception {
        p = "src/test/resources/control-shapes.svg";
    }

    @Test
    public void testRead() throws IOException, InterruptedException, XMLStreamException {

        log.info("start test:{}", this.getClass().getSimpleName());
        log.debug("Working dir: {}", Paths.get(".").toAbsolutePath().toString());

        svgLoader.loadSvg(p);

        expectedItemsLoaded.await(5, TimeUnit.MINUTES);
        log.info("end test:{}", this.getClass().getSimpleName());

    }

}