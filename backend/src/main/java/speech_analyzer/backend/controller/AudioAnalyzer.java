package speech_analyzer.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import speech_analyzer.backend.Dto.SpeechAnalysisDto;
import speech_analyzer.backend.services.AudioAnalyzerService;

@RestController
public class AudioAnalyzer {

    @Autowired
    private AudioAnalyzerService audioAnalyzerService;

    @PostMapping(value="/analyze", consumes = "text/plain")
    public ResponseEntity<SpeechAnalysisDto> analyze(@RequestBody String message){
        SpeechAnalysisDto response = audioAnalyzerService.generateResponse(message);
        if(response != null){
            return new ResponseEntity<SpeechAnalysisDto>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}

