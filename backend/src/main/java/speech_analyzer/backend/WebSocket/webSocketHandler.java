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

import java.io.*;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class webSocketHandler extends BinaryWebSocketHandler {

    WebSocketAudioPublisher publisher = new WebSocketAudioPublisher();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("the connection is established " + session.getId());
        TranscribeStreamingAsyncClient transcribeClient = TranscribeStreamingAsyncClient.builder()
                .region(Region.US_EAST_1) // Or your desired region
                .build();

        StartStreamTranscriptionRequest request = StartStreamTranscriptionRequest.builder()
                .languageCode(LanguageCode.EN_US)
                .mediaEncoding(MediaEncoding.OGG_OPUS) // use PCM for streaming
                .mediaSampleRateHertz(16000)
                .build();

        StartStreamTranscriptionResponseHandler responseHandler =
                StartStreamTranscriptionResponseHandler.builder()
                        .onResponse(r -> {
                            System.out.println("Received Initial response");
                        })
                        .subscriber(event -> {
                            List<Result> results = ((TranscriptEvent) event).transcript().results();
                            if (results.size() > 0) {
                                if (!results.get(0).alternatives().get(0).transcript().isEmpty()) {
                                    System.out.println(results.get(0).alternatives().get(0).transcript());
                                }
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
        buffer.get(audioBytes);
//        for (int i=0; i<audioBytes.length;i++){
//            System.out.print(audioBytes[i]);
//        }
//        System.out.println();
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