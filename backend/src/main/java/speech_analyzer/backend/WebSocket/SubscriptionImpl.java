package speech_analyzer.backend.WebSocket;

import lombok.SneakyThrows;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.transcribestreaming.model.AudioEvent;
import software.amazon.awssdk.services.transcribestreaming.model.AudioStream;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class SubscriptionImpl implements Subscription {
    BlockingQueue<byte[]> queue;
    private final Subscriber<? super AudioStream> subscriber;
    private final AtomicBoolean cancelled = new AtomicBoolean(false);

    SubscriptionImpl(Subscriber<? super AudioStream> s, BlockingQueue<byte[]> queue) {
        this.subscriber = s;
        this.queue = queue;
    }

    @SneakyThrows
    @Override
    public void request(long n) {
        for (int i = 0; i < n && !cancelled.get(); i++) {
            byte[] chunk = queue.poll(3, TimeUnit.SECONDS); // never blocks
            if (chunk == null) {
                subscriber.onComplete();
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

    }

}
