package speech_analyzer.backend.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import speech_analyzer.backend.dto.UserSignupDto;
import speech_analyzer.backend.services.SignupService;

@RestController
public class SignUp
{

    @Autowired
    private SignupService signupService;

    @PostMapping("/signup")
    public ResponseEntity<HttpStatus> signup(@Valid @RequestBody UserSignupDto user){
        String name = signupService.createUser(user);
        if (!name.isEmpty()){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
