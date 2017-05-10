package marcio.rx.io;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Static class that provided a file path returns an emitter of chunks read from that file
 */
public class AsyncFileReader {
    private static final Logger log = LoggerFactory.getLogger(AsyncFileReader.class);

    private static final int READ_BUFFER_SIZE = (int) Math.pow(2, 12); //2^5=32;2^10=1024;

    public static Flowable<String> read(final String p) throws IOException, InterruptedException {
        log.debug("method init - reading chunks of {}(bytes)", READ_BUFFER_SIZE);

        final Path path = Paths.get(p);
        log.debug("path:{}",path);
        // create a dedicated singleThreadExecutor for this file read operation
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final AsynchronousFileChannel afc = AsynchronousFileChannel.open(path, new HashSet<StandardOpenOption>(Arrays.asList(StandardOpenOption.READ)), executorService);
        final int fileSize = (int) afc.size();
        log.debug("fileSize:{}(bytes)", fileSize);

        final ByteBuffer dataBuffer = ByteBuffer.allocate(READ_BUFFER_SIZE);

        final Attachment attach = new Attachment();
        attach.pointer = 0;
        attach.asyncChannel = afc;
        attach.buffer = dataBuffer;

        final CompletionHandler ch = new CompletionHandler<Integer, Attachment>() {
            @Override
            public void completed(Integer result, Attachment attachment) {
                log.debug("{}, {}", new Object[]{result, attachment.buffer.toString()});
                if (result != -1) {
                    attachment.buffer.flip(); // flip the buffer for reading
                    byte[] bytes = new byte[attachment.buffer.remaining()]; // create a byte array the length of the number of bytes written to the buffer
                    attachment.buffer.get(bytes); // read the bytes that were written

                    String packet = new String(bytes);
                    log.trace("content read:{}", packet);
                    attachment.flowableEmitter.onNext(packet);

                    attachment.buffer.clear();

                    //recursively read until no data left to be read
                    attachment.pointer += result;
                    attachment.asyncChannel.read(attachment.buffer, attachment.pointer, attachment, this);
                } else {
                    // shutdown executor service
                    executorService.shutdown();
                    try {
                        afc.close();
                    } catch (IOException e) {
                        log.warn("unexpected exception while closing asyncronous file channel: {}", e);
                    }
                }
            }

            @Override
            public void failed(Throwable exc, Attachment attachment) {
                log.warn("unexpected error while reading file: {}, {}", new Object[]{exc, attachment});
            }
        };

        final Flowable<String> flowable = Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                attach.flowableEmitter = e;
                afc.read(attach.buffer, attach.pointer, attach, ch);
            }
        }, BackpressureStrategy.ERROR);

        return flowable;
    }

    /**
     * Attachment holds the context to be shared between CompletionHandler of AsynchronousFileChannel::read and the caller
     */
    private static class Attachment {
        public int pointer = 0;
        public ByteBuffer buffer;
        public AsynchronousFileChannel asyncChannel;
        public FlowableEmitter flowableEmitter;
    }
}
