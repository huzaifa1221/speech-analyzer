package speech_analyzer.backend.WebSocket;

import lombok.SneakyThrows;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.transcribestreaming.model.AudioEvent;
import software.amazon.awssdk.services.transcribestreaming.model.AudioStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class SubscriptionImpl implements Subscription {
    BlockingQueue<byte[]> queue;
    private static final int CHUNK_SIZE_IN_BYTES = 1024 * 1;
    private final Subscriber<? super AudioStream> subscriber;
    private ExecutorService executor = Executors.newFixedThreadPool(1);
    private AtomicLong demand = new AtomicLong(0);
    private final AtomicBoolean cancelled = new AtomicBoolean(false);

    SubscriptionImpl(Subscriber<? super AudioStream> s, BlockingQueue<byte[]> queue) {
        this.subscriber = s;
        this.queue = queue;
    }

    @SneakyThrows
    @Override
    public void request(long n) {
//        System.out.println("requesting for chunks :"+ n);
        for (int i = 0; i < n && !cancelled.get(); i++) {
            byte[] chunk = queue.poll(5,TimeUnit.SECONDS); // never blocks
//            System.out.println("this is the chunk length retrieved from queue "+chunk.length);
            if (chunk == null) {
                break; // nothing yet, return quickly
            }
            AudioEvent event = AudioEvent.builder()
                    .audioChunk(SdkBytes.fromByteArray(chunk))
                    .build();
            subscriber.onNext(event);
        }
    }

    @Override
    public void cancel() {
        System.out.println("cancelled is called");
//        executor.shutdown();
    }

//    private AudioEvent audioEventFromBuffer(ByteBuffer buffer) {
//        return AudioEvent.builder().audioChunk(SdkBytes.fromByteBuffer(buffer)).build();
//    }
}
