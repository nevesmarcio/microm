package marcio.batik;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import helper.GameTest;
import marcio.batik.game1.LoadedActor;
import marcio.transform.AffineTransformation;
import marcio.xml.IXmlNodeEmitter;
import marcio.xml.LibgdxXmlParserService;
import marcio.xml.codec.XmlNode;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;


public class SvgServiceTest extends GameTest {

    private static final Logger log = LoggerFactory.getLogger(SvgServiceTest.class);

    final private LibgdxXmlParserService libgdxXmlParserService = new LibgdxXmlParserService();
    final private SvgService svgService = new SvgService(new AffineTransformation(), new IAppendable() {
        @Override
        public void append(LoadedActor loadedActor) {
            log.info("-->appending {}", loadedActor);
        }
    });

    private FileHandle fileHandle;

    @Before
    public void setUp() {
        fileHandle = Gdx.files.internal("control-shapes.svg");

        libgdxXmlParserService.setXmlNodeEmitter(new IXmlNodeEmitter() {
            @Override
            public void emit(XmlNode xmlNode) {
                System.out.println("\t\t\t>>>Emitting: " + xmlNode.toString());
                svgService.feedInput(xmlNode);
            }
        });

    }

    @Test
    public void testRead() throws Exception {

        log.info("start test:{}", this.getClass().getSimpleName());
        log.debug("Working dir: {}", Paths.get(".").toAbsolutePath().toString());

        final String readContent = fileHandle.readString();
        libgdxXmlParserService.feedInput(readContent.getBytes(), 0, readContent.getBytes().length);

        Thread.sleep(1000);
        log.info("end test:{}", this.getClass().getSimpleName());

    }

}