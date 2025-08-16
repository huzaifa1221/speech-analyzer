package speech_analyzer.backend.WebSocket;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.transcribestreaming.model.AudioEvent;
import software.amazon.awssdk.services.transcribestreaming.model.AudioStream;

import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class WebSocketAudioPublisher implements Publisher<AudioStream>
{
    private Subscriber<? super AudioStream> subscriber;
    private final BlockingQueue<byte[]> queue = new LinkedBlockingQueue<>();

    public void offer(byte[] audioChunk) {
        queue.offer(audioChunk);
    }

    private void sendNext() {
        byte[] chunk;
        while ((chunk = queue.poll()) != null) {
            AudioEvent event = AudioEvent.builder()
                    .audioChunk(SdkBytes.fromByteArray(chunk))
                    .build();
            subscriber.onNext(event);
        }
    }

    public void complete() {
        if (subscriber != null) {
            subscriber.onComplete();
        }
    }

    @Override
    public void subscribe(Subscriber<? super AudioStream> s) {
        this.subscriber = s;
        s.onSubscribe(new Subscription() {
            @Override
            public void request(long n) {
                sendNext();
            }

            @Override
            public void cancel() {}
        });
    }
}
