package speech_analyzer.backend.WebSocket;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import software.amazon.awssdk.services.transcribestreaming.model.AudioStream;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AudioStreamPublisher implements Publisher<AudioStream> {
    private static Subscription currentSubscription;
    BlockingQueue<byte[]> queue = new LinkedBlockingQueue<>();

    public void addInQueue(byte[] audioBytes){
        try {
            queue.put(audioBytes);
//            System.out.println("audio bytes added "+audioBytes.length);
        } catch (InterruptedException e) {
            System.out.println("error occured while adding items in queue");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void subscribe(Subscriber<? super AudioStream> s) {
        if (this.currentSubscription == null) {
            this.currentSubscription = new SubscriptionImpl(s, queue);
        } else {
            this.currentSubscription.cancel();
            this.currentSubscription = new SubscriptionImpl(s, queue);
        }
        s.onSubscribe(currentSubscription);
        System.out.println("the aws has now subscribed to the publisher");
    }


}
