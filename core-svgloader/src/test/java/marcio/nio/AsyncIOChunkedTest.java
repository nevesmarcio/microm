package marcio.nio;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;


public class AsyncIOChunkedTest {

    private static final Logger log = LoggerFactory.getLogger(AsyncIOChunkedTest.class);

    private AsyncIOChunked asyncIOChunked;
    private String p;

    @Before
    public void setUp() throws Exception {
        asyncIOChunked = new AsyncIOChunked();
        p = "src/test/resources/employee.xml";
    }

    @Test
    public void testRead() throws IOException, InterruptedException {

        log.info("start test:{}", this.getClass().getSimpleName());
        log.debug("Working dir: {}", Paths.get(".").toAbsolutePath().toString());

        asyncIOChunked.asyncRead(p, new ChunkReadHandler() {
            @Override
            public void handle(String chunk) {
                System.out.print(chunk);
            }
        });

        Thread.sleep(1000);
        log.info("end test:{}", this.getClass().getSimpleName());

    }

}