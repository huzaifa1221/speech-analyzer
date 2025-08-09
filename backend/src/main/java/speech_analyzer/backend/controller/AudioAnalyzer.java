package speech_analyzer.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import speech_analyzer.backend.Dto.SpeechAnalysisDto;
import speech_analyzer.backend.services.AudioAnalyzerService;

@RestController
public class AudioAnalyzer {

    @Autowired
    private AudioAnalyzerService audioAnalyzerService;

    @PostMapping("/analyze")
    public ResponseEntity<SpeechAnalysisDto> analyze(@RequestParam("file") MultipartFile audio){
        SpeechAnalysisDto response = audioAnalyzerService.AnalyzeAudio(audio);
        if(response != null){
            return new ResponseEntity<SpeechAnalysisDto>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}

