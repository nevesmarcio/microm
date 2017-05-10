package marcio.rx.io;

import com.badlogic.gdx.Gdx;
import helper.GameTest;
import io.reactivex.functions.Consumer;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class AsyncFileReaderTest extends GameTest {
    private static final Logger logger = LoggerFactory.getLogger(AsyncFileReaderTest.class);

    private String p;

    @Before
    public void setUp() throws Exception {
        p = Gdx.files.local("./build/resources/test/test.svg").path();
    }

    @Test
    public void testRead() throws IOException, InterruptedException {
        AsyncFileReader.read(p).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                logger.info("1>>{}", s);
            }
        });

        AsyncFileReader.read(p).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                logger.info("2>>{}", s);
            }
        });

        Thread.sleep(1000);//1 second to give some time for the subscribers to actually do some work
    }

}