package speech_analyzer.backend.WebSocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.transcribestreaming.TranscribeStreamingAsyncClient;
import software.amazon.awssdk.services.transcribestreaming.model.*;
import speech_analyzer.backend.services.AudioAnalyzerService;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public class webSocketHandler extends BinaryWebSocketHandler {

    AudioStreamPublisher publisher = new AudioStreamPublisher();
    WebSocketSession session;

    public StartStreamTranscriptionResponseHandler responseHandler(){
        AtomicReference<StringBuilder> transcriptBuilder = new AtomicReference<>(new StringBuilder());
        return StartStreamTranscriptionResponseHandler.builder()
                        .onResponse(r -> {})
                        .subscriber(event -> {
                            List<Result> results = ((TranscriptEvent) event).transcript().results();
                            if (results.size() > 0) {
                                if (!results.get(0).alternatives().get(0).transcript().isEmpty()) {
                                    for (Result result : results) {
                                        if (!result.isPartial()) {
                                            for (Alternative alt : result.alternatives()) {
                                                transcriptBuilder.get()
                                                        .append(alt.transcript())
                                                        .append(" ");
                                            }
                                        }
                                    }
                                }
                            }
                        })
                        .onError(throwable -> {
                            System.out.println("this error is coming from the response handler " +throwable);
                        })
                        .onComplete(() -> {
                            System.out.println("transcription completed");
                            String fullTranscript = transcriptBuilder.get().toString().trim();
                            System.out.println(fullTranscript);
                                    try {
                                        session.sendMessage(new TextMessage(fullTranscript));
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                        )
                        .build();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws ExecutionException, InterruptedException {
        this.session = session;
        System.out.println("the connection is established " + session.getId());
        TranscribeStreamingAsyncClient transcribeClient = TranscribeStreamingAsyncClient.builder()
                .region(Region.US_EAST_1) // Or your desired region
                .build();

        StartStreamTranscriptionRequest request = StartStreamTranscriptionRequest.builder()
                .languageCode(LanguageCode.EN_US)
                .mediaEncoding(MediaEncoding.PCM) // use PCM for streaming
                .mediaSampleRateHertz(16000)
                .build();

        CompletableFuture<Void> result = transcribeClient.startStreamTranscription(request, publisher, responseHandler());
        result.whenComplete((r, t) -> {
            if (t != null) {
                System.out.println("Transcribe stream failed"+ t);
            } else {
                try {
                    session.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Transcribe stream completed normally");
            }
        });
    }

    @Override
    public void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        ByteBuffer buffer = message.getPayload();
        byte[] audioBytes = new byte[buffer.remaining()];
        buffer.get(audioBytes);
        publisher.addInQueue(audioBytes);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        publisher.closeTranscribeConnection();
        System.out.println("websocket connection closed: "+session);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        exception.printStackTrace();
    }
}