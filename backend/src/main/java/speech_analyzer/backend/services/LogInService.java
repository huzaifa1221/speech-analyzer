package speech_analyzer.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import speech_analyzer.backend.dto.UserLoginDto;
import speech_analyzer.backend.entity.User;
import speech_analyzer.backend.repository.UserRepository;

@Service
public class LogInService {

    @Autowired
    private UserRepository userRepository;

    public User login(UserLoginDto userinfo){
        User foundUser = userRepository.findByUsername(userinfo.getUsername());
        if(foundUser != null){
            if (foundUser.getPassword().equals(userinfo.getPassword())){
                return foundUser;
            }else{
                return null;
            }
        }
        return null;
    }
}
