package _tutorial.nio;

import com.badlogic.gdx.Gdx;
import helper.GameTest;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.*;

/**
 * http://niklasschlimm.blogspot.com.es/2012/04/java-7-asynchronous-file-channels-part.html
 */
public class AsyncIOTest extends GameTest {
    private static final Logger log = LoggerFactory.getLogger(AsyncIOTest.class);

    private String p;

    @Before
    public void setUp() throws Exception {
        log.debug(Gdx.files.getLocalStoragePath());
        log.debug(Gdx.files.getExternalStoragePath());

        p = Gdx.files.local("test.txt").path();
    }

    @Test
    public void testAsyncWriteWithHandler() throws Exception {
        log.debug("testAsyncWriteWithHandler");

        Path path = Paths.get(p);
        AsynchronousFileChannel afc = AsynchronousFileChannel.open(path, WRITE, CREATE);
        WriteHandler handler = new WriteHandler();
        ByteBuffer dataBuffer = getDataBuffer();
        Attachment attach = new Attachment();
        attach.asyncChannel = afc;
        attach.buffer = dataBuffer;
        attach.path = path;

        afc.write(dataBuffer, 0, attach, handler);

        log.info("Sleeping for 5  seconds...");
        Thread.sleep(5000);
    }

    @Test
    public void testAsyncReadWithHandler() throws Exception {
        log.debug("testAsyncReadWithHandler");

        Path path = Paths.get(p);
        AsynchronousFileChannel afc = AsynchronousFileChannel.open(path, READ);
        ReadHandler handler = new ReadHandler();
        int fileSize = (int) afc.size();
        ByteBuffer dataBuffer = ByteBuffer.allocate(fileSize);

        Attachment attach = new Attachment();
        attach.asyncChannel = afc;
        attach.buffer = dataBuffer;
        attach.path = path;

        afc.read(dataBuffer, 0, attach, handler);

        log.info("Sleeping for 5  seconds...");
        Thread.sleep(5000);

    }


    class Attachment {
        public Path path;
        public ByteBuffer buffer;
        public AsynchronousFileChannel asyncChannel;
    }

    public static ByteBuffer getDataBuffer() {
        String lineSeparator = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();
        sb.append("test");
        sb.append(lineSeparator);
        sb.append("test");
        sb.append(lineSeparator);
        String str = sb.toString();
        Charset cs = Charset.forName("UTF-8");
        ByteBuffer bb = ByteBuffer.wrap(str.getBytes(cs));
        return bb;
    }

    class WriteHandler implements CompletionHandler<Integer, Attachment> {
        @Override
        public void completed(Integer result, Attachment attach) {
            log.info(String.format("%s bytes written  to  %s%n", result,
                    attach.path.toAbsolutePath()));
            try {
                attach.asyncChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void failed(Throwable e, Attachment attach) {
            log.error(String.format("Read operation  on  %s  file failed."
                    + "The  error is: %s%n", attach.path, e.getMessage()));
            try {
                // Close the channel
                attach.asyncChannel.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    class ReadHandler implements CompletionHandler<Integer, Attachment> {
        @Override
        public void completed(Integer result, Attachment attach) {
            log.info(String.format("%s bytes read   from  %s%n", result, attach.path));
            log.info(String.format("Read data is:%n"));
            byte[] byteData = attach.buffer.array();
            Charset cs = Charset.forName("UTF-8");
            String data = new String(byteData, cs);
            log.info(data);
            try {
                // Close the channel
                attach.asyncChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void failed(Throwable e, Attachment attach) {
            log.error(String.format("Read operation  on  %s  file failed."
                    + "The  error is: %s%n", attach.path, e.getMessage()));
            try {
                // Close the channel
                attach.asyncChannel.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

}