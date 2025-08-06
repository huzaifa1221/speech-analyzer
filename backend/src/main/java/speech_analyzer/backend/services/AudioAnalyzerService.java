package speech_analyzer.backend.services;

import org.springframework.stereotype.Service;

@Service
public class AudioAnalyzerService
{
    public void AnalyzeAudio(byte[] rawData){
        System.out.println("the audio is received in the service");
    }

//    public String uploadAudioToS3()
}
