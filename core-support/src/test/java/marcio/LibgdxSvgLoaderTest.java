package marcio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import helper.GameTest;
import marcio.batik.IAppendable;
import marcio.batik.game1.LoadedActor;
import marcio.transform.AffineTransformation;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class LibgdxSvgLoaderTest extends GameTest {

    private static final Logger log = LoggerFactory.getLogger(LibgdxSvgLoaderTest.class);

    private CountDownLatch expectedItemsLoaded = new CountDownLatch(7);
    private LibgdxSvgLoader svgLoader = new LibgdxSvgLoader(new AffineTransformation(), new IAppendable() {
        @Override
        public void append(LoadedActor loadedActor) {
            log.info("-->appending {}", loadedActor);
            expectedItemsLoaded.countDown();
        }
    });

    private FileHandle fileHandle;

    @Before
    public void setUp() throws Exception {
//        fileHandle = Gdx.files.internal("src/test/resources/control-shapes.svg");
        fileHandle = Gdx.files.internal("control-shapes.svg");
    }

    @Test
    public void testRead() throws Exception {

        log.info("start test:{}", this.getClass().getSimpleName());
        log.debug("Working dir: {}", Paths.get(".").toAbsolutePath().toString());

        svgLoader.loadSvg(fileHandle);

        expectedItemsLoaded.await(15, TimeUnit.MINUTES);
        log.info("end test:{}", this.getClass().getSimpleName());

    }

}