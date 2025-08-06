package speech_analyzer.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import speech_analyzer.backend.dto.UserLoginDto;
import speech_analyzer.backend.entity.User;
import speech_analyzer.backend.services.LogInService;

@RestController
public class LogIn
{
    @Autowired
    private LogInService logInService;

    @PostMapping("login")
    public ResponseEntity<HttpStatus> login(@RequestBody UserLoginDto userInfo){
        User user = logInService.login(userInfo);
        if(user != null){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
