package speech_analyzer.backend.services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.transcribe.AmazonTranscribe;
import com.amazonaws.services.transcribe.AmazonTranscribeClientBuilder;
import com.amazonaws.services.transcribe.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import speech_analyzer.backend.Dto.SpeechAnalysisDto;

@Service
public class AudioAnalyzerService
{
    @Value("${aws.s3.input-bucket-name}")
    private String bucketName;

    @Value("${aws.region}")
    private String awsRegion;

    public SpeechAnalysisDto AnalyzeAudio(MultipartFile audio){
        System.out.println("the audio is received in the service");
        String randomString = UUID.randomUUID().toString();
        String s3Uri = uploadAudioToS3(audio, randomString);
        String transcript = transcribeAudio(s3Uri, randomString);
        SpeechAnalysisDto response = generateResponse(transcript);
        return response;
    }

    public SpeechAnalysisDto generateResponse(String transcript){

        Client client = new Client();

        String prompt =
                "You are an AI speech analysis expert. \n" +
                        "You will receive a transcript of a spoken speech. \n" +
                        "Analyze it strictly and return ONLY a JSON object in the exact structure below.\n\n" +
                        "JSON structure:\n" +
                        "{\n" +
                        "  \"confidence_analysis\": {\n" +
                        "    \"score\": 0,\n" +
                        "    \"feedback\": \"\"\n" +
                        "  },\n" +
                        "  \"improvement_suggestions\": [\n" +
                        "    \"\"\n" +
                        "  ],\n" +
                        "  \"better_words_sentences\": [\n" +
                        "    {\n" +
                        "      \"original\": \"\",\n" +
                        "      \"suggestion\": \"\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"overall_rating\": 0\n" +
                        "}\n\n" +
                        "Instructions:\n" +
                        "- \"confidence_analysis.score\" should be an integer from 1-10 based on tone and assertiveness in the transcript.\n" +
                        "- \"confidence_analysis.feedback\" should be 1-3 sentences describing confidence level and delivery style.\n" +
                        "- \"improvement_suggestions\" should be 3-5 short actionable tips to improve delivery, pacing, or engagement.\n" +
                        "- \"better_words_sentences\" should replace weak words or awkward phrases with stronger, more concise versions.\n" +
                        "- \"overall_rating\" is an integer 1-10 reflecting the speech’s overall quality.\n" +
                        "- Do not include any text outside the JSON object.\n\n" +
                        "Here is the transcript: " + transcript;

        GenerateContentResponse response = client.models.generateContent(
                                                    "gemini-2.5-flash",
                                                    prompt,
                                                    null);
        String rawJson = response.text();
        String cleanedJson = rawJson
                .replaceAll("^```[a-zA-Z]*", "")  // remove starting ```json or ```
                .replaceAll("```$", "")           // remove ending ```
                .trim();
        ObjectMapper mapper = new ObjectMapper();
        try {
            SpeechAnalysisDto dto = mapper.readValue(cleanedJson, SpeechAnalysisDto.class);
            return dto;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String transcribeAudio(String s3Uri, String randomString){
        System.out.println("starting transcription");
        AmazonTranscribe transcribeClient = AmazonTranscribeClientBuilder.standard()
                .withRegion(awsRegion)
                .build();

        String transcriptionJobName = randomString;
        Media media = new Media().withMediaFileUri(s3Uri);

        StartTranscriptionJobRequest request = new StartTranscriptionJobRequest()
                .withTranscriptionJobName(transcriptionJobName)
                .withLanguageCode("en-US")
                .withMediaFormat("webm")
                .withMedia(media);

        transcribeClient.startTranscriptionJob(request);

        while (true){
            GetTranscriptionJobRequest getJobRequest = new GetTranscriptionJobRequest()
                    .withTranscriptionJobName(transcriptionJobName);
            GetTranscriptionJobResult result = transcribeClient.getTranscriptionJob(getJobRequest);
            TranscriptionJob job = result.getTranscriptionJob();
            String status = job.getTranscriptionJobStatus();
            if(status.equals("COMPLETED")){
                String transcriptFileUri = job.getTranscript().getTranscriptFileUri();
                // Download the JSON file
                try (InputStream in = new URL(transcriptFileUri).openStream()) {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode root = mapper.readTree(in);
                    // Extract the transcript text
                    return root.path("results").path("transcripts").get(0).path("transcript").asText();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                Thread.sleep(1000);
                System.out.println("status: "+status);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String uploadAudioToS3(MultipartFile audio, String randomString) {
        String objectKey = randomString+".webm";
        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(awsRegion)
                    .build();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(audio.getContentType());
            PutObjectRequest request = new PutObjectRequest(bucketName, objectKey, audio.getInputStream(), metadata);
            request.setMetadata(metadata);
            s3Client.putObject(request);
            System.out.println("audio save successfully in the s3 bucket and uri returned");
            return s3Client.getUrl(bucketName, objectKey).toString();
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        } catch (IOException e) {
        }
        return null;
    }
}
