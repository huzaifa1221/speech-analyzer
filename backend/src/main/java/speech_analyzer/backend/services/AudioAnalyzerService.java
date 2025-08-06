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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
public class AudioAnalyzerService
{
    @Value("${aws.s3.input-bucket-name}")
    private String bucketName;

    @Value("${aws.s3.output-bucket-name}")
    private String outputBucketName;

    @Value("${aws.region}")
    private String awsRegion;

    public void AnalyzeAudio(MultipartFile audio, String username, String title){
        System.out.println("the audio is received in the service");
        String s3Uri = uploadAudioToS3(audio, username, title);
        String transcript = transcribeAudio(s3Uri, title);
        System.out.println(transcript);
    }

    public String transcribeAudio(String s3Uri, String title){
        System.out.println("starting transcription");
        AmazonTranscribe transcribeClient = AmazonTranscribeClientBuilder.standard()
                .withRegion(awsRegion)
                .build();

        String transcriptionJobName = title;
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
                return job.getTranscript().getTranscriptFileUri();
            }
            try {
                Thread.sleep(2000);
                System.out.println("status: "+status);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String uploadAudioToS3(MultipartFile audio, String username, String title) {
        String objectKey = username+"/"+title+".webm";
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
            // Handle the failure (log, rethrow, fallback, etc.)
        }
        return null;
    }
}
