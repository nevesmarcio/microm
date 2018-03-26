package marcio;

import marcio.batik.IAppendable;
import marcio.batik.game1.LoadedActor;
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


public class SvgLoaderTest {

    private static final Logger log = LoggerFactory.getLogger(SvgLoaderTest.class);

    private SvgLoader svgLoader = new SvgLoader(new IAppendable() {
        @Override
        public void append(LoadedActor loadedActor) {
            log.info("-->appending {}", loadedActor);
        }
    });

    private String p;

    @Before
    public void setUp() throws Exception {
        p = "src/test/resources/full-level.svg";
    }

    @Test
    public void testRead() throws IOException, InterruptedException, XMLStreamException {

        log.info("start test:{}", this.getClass().getSimpleName());
        log.debug("Working dir: {}", Paths.get(".").toAbsolutePath().toString());

        svgLoader.loadSvg(p);

        Thread.sleep(2000);
        log.info("end test:{}", this.getClass().getSimpleName());

    }

}