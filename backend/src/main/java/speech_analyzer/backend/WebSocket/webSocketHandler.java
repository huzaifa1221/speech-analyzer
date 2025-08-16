package speech_analyzer.backend.WebSocket;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.transcribestreaming.TranscribeStreamingAsyncClient;
import software.amazon.awssdk.services.transcribestreaming.model.*;

import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;

public class webSocketHandler extends BinaryWebSocketHandler {

    WebSocketAudioPublisher publisher = new WebSocketAudioPublisher();

    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        System.out.println("the connection is established " + session.getId());
        TranscribeStreamingAsyncClient transcribeClient = TranscribeStreamingAsyncClient.builder()
                .region(Region.US_EAST_1) // Or your desired region
                .build();

        StartStreamTranscriptionRequest request = StartStreamTranscriptionRequest.builder()
                .languageCode(LanguageCode.EN_US)
                .mediaEncoding(MediaEncoding.PCM) // use PCM for streaming
                .mediaSampleRateHertz(16000)
                .build();

        StartStreamTranscriptionResponseHandler responseHandler =
                StartStreamTranscriptionResponseHandler.builder()
                        .subscriber(event -> {
                            if (event instanceof TranscriptEvent te) {
                                te.transcript().results().forEach(r ->
                                        r.alternatives().forEach(a ->
                                                System.out.println((r.isPartial() ? "[partial] " : "[final] ") + a.transcript())
                                        )
                                );
                            }
                        })
                        .onError(Throwable::printStackTrace)
                        .onComplete(() -> System.out.println("Transcription complete"))
                        .build();


        CompletableFuture<Void> result = transcribeClient.startStreamTranscription(request, publisher, responseHandler);
    }

    @Override
    public void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        ByteBuffer buffer = message.getPayload();
        byte[] audioBytes = new byte[buffer.remaining()];
        publisher.offer(audioBytes);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("the connection is closed " + session.getId());
        publisher.complete();
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        exception.printStackTrace();
    }

}
