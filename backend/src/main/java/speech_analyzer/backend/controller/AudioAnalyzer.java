package speech_analyzer.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import speech_analyzer.backend.services.AudioAnalyzerService;

@RestController
public class AudioAnalyzer {

    @Autowired
    private AudioAnalyzerService audioAnalyzerService;

    @PostMapping("/analyze")
    public void analyze(@RequestParam("file") MultipartFile audio){
        audioAnalyzerService.AnalyzeAudio(audio);
    }
}

