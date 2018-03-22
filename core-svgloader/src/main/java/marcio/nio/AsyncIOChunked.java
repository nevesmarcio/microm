package marcio.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncIOChunked {
    private static final Logger log = LoggerFactory.getLogger(AsyncIOChunked.class);

    private final int READ_BUFFER_SIZE = (int) Math.pow(2, 12); //2^5=32;2^10=1024;
    private final ExecutorService es = Executors.newSingleThreadExecutor();



    public void asyncRead(final String file, final ChunkReadHandler readHandler) throws IOException{
        log.debug("testAsyncXMLReadSingleThreadRecursive");

        Path path = Paths.get(file);
        AsynchronousFileChannel afc = AsynchronousFileChannel.open(path, new HashSet<StandardOpenOption>(Arrays.asList(StandardOpenOption.READ)), es);
        int fileSize = (int) afc.size();
        log.debug("Reading file in Path='{}', Size={}bytes. Will read it in {} chunks.", path.toAbsolutePath().toString(),fileSize, (float) Files.size(path) / (float) READ_BUFFER_SIZE);

        ByteBuffer dataBuffer = ByteBuffer.allocate(READ_BUFFER_SIZE);

        final Attachment attach = new Attachment();
        attach.pointer = 0;
        attach.asyncChannel = afc;
        attach.buffer = dataBuffer;

        CompletionHandler ch = new CompletionHandler<Integer, Attachment>() {
            @Override
            public void completed(Integer result, Attachment attachment) {
                log.debug("{}, {}", new Object[]{result, attachment.buffer.toString()});
                if (result != -1) {
                    attachment.pointer += result;

                    attachment.buffer.flip(); // flip the buffer for reading
                    byte[] bytes = new byte[attachment.buffer.remaining()]; // create a byte array the length of the number of bytes written to the buffer
                    attachment.buffer.get(bytes); // read the bytes that were written

                    String packet = new String(bytes);
                    if (log.isTraceEnabled()) log.trace(packet);
                    readHandler.handle(packet);

                    attachment.buffer.clear();

                    attachment.asyncChannel.read(attachment.buffer, attachment.pointer, attachment, this);
                } else {
                    log.debug("no further reads");
                }
            }

            @Override
            public void failed(Throwable exc, Attachment attachment) {
                log.debug("{}, {}", new Object[]{exc, attachment});
            }
        };

        afc.read(attach.buffer, attach.pointer, attach, ch);
    }

    private class Attachment {
        private int pointer = 0;
        private AsynchronousFileChannel asyncChannel;
        private ByteBuffer buffer;
    }
}
