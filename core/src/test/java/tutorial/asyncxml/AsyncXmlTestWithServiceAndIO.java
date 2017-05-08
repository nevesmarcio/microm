package tutorial.asyncxml;

import com.badlogic.gdx.Gdx;
import helper.GameTest;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tutorial.asyncio.AsyncIOTest;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static java.nio.file.StandardOpenOption.READ;

public class AsyncXmlTestWithServiceAndIO extends GameTest {
    private static final Logger log = LoggerFactory.getLogger(AsyncXmlTestWithServiceAndIO.class);

    private String p;
    private final int READ_BUFFER_SIZE = (int) Math.pow(2, 10); //2^5=32

    @Before
    public void setUp() throws Exception {
        p = Gdx.files.local("./build/resources/test/test.svg").path();
    }

    @Test
    public void testAsyncXMLReadSingleThread() throws IOException, InterruptedException {
        log.debug("testAsyncXMLReadSingleThread");

        final AsyncXmlService asyncXmlService = new AsyncXmlService();

        Path path = Paths.get(p);
        AsynchronousFileChannel afc = AsynchronousFileChannel.open(path, new HashSet<StandardOpenOption>(Arrays.asList(StandardOpenOption.READ)),Executors.newSingleThreadExecutor());
//        AsyncIOTest.ReadHandler handler = new AsyncIOTest.ReadHandler();
        int fileSize = (int) afc.size();
        ByteBuffer dataBuffer = ByteBuffer.allocate(READ_BUFFER_SIZE);

        final Attachment attach = new Attachment();
        attach.pointer=0;
        attach.asyncChannel = afc;
        attach.buffer = dataBuffer;
        attach.path = path;


        CompletionHandler ch = new CompletionHandler<Integer, Attachment>() {
            @Override
            public void completed(Integer result, Attachment attachment) {
                log.debug("{}, {}", new Object[]{result, attachment.buffer.toString()});
                if (result!=-1) {
                    attachment.pointer+=result;
                    attachment.buffer.clear();

                    attachment.countDownLatch.countDown();
                }
            }

            @Override
            public void failed(Throwable exc, Attachment attachment) {
                log.debug("{}, {}", new Object[]{exc, attachment});
            }
        };

        do {
            attach.countDownLatch = new CountDownLatch(1);
            afc.read(attach.buffer, attach.pointer, attach, ch);

            attach.countDownLatch.await();
        } while (fileSize>attach.pointer);

        while (true) {
            System.out.println("***");
            Thread.sleep(1000);
        }

    }

    @Test
    public void testAsyncXMLReadInneficientThreads() throws IOException, InterruptedException {
        log.debug("testAsyncXMLReadInneficientThreads");

        final AsyncXmlService asyncXmlService = new AsyncXmlService();

        Path path = Paths.get(p);
//        AsynchronousFileChannel afc = AsynchronousFileChannel.open(path, READ);
        AsynchronousFileChannel afc = AsynchronousFileChannel.open(path, new HashSet<StandardOpenOption>(Arrays.asList(StandardOpenOption.READ)),Executors.newSingleThreadExecutor());
//        AsyncIOTest.ReadHandler handler = new AsyncIOTest.ReadHandler();
        int fileSize = (int) afc.size();
        ByteBuffer dataBuffer = ByteBuffer.allocate(READ_BUFFER_SIZE);

        final Attachment attach = new Attachment();
        attach.pointer=0;
        attach.asyncChannel = afc;
        attach.buffer = dataBuffer;
        attach.path = path;

        CompletionHandler ch = new CompletionHandler<Integer, Attachment>() {
            @Override
            public void completed(Integer result, Attachment attachment) {
                log.debug("{}, {}", new Object[]{result, attachment.buffer.toString()});
                if (result!=-1) {
                    attachment.pointer+=result;
                    attachment.buffer.clear();

                    attachment.asyncChannel.read(attachment.buffer, attachment.pointer,attachment, this);
                }
            }

            @Override
            public void failed(Throwable exc, Attachment attachment) {
                log.debug("{}, {}", new Object[]{exc, attachment});
            }
        };

        afc.read(attach.buffer, attach.pointer, attach, ch);

        while (true) {
            Thread.sleep(1000);
        }

    }

    class Attachment {
        public CountDownLatch countDownLatch;
        public int pointer=0;
        public Path path;
        public ByteBuffer buffer;
        public AsynchronousFileChannel asyncChannel;
    }

}
