package speech_analyzer.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import speech_analyzer.backend.dto.UserSignupDto;
import speech_analyzer.backend.entity.User;
import speech_analyzer.backend.repository.UserRepository;

@Service
public class SignupService {

    @Autowired
    private UserRepository userRepository;

    public User signupDtoToEntity(UserSignupDto dto){
        User user = new User();
        user.setName(dto.getName());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        return user;
    }

    public String createUser(UserSignupDto userinfo){
        User user = signupDtoToEntity(userinfo);
        try{
            userRepository.save(user);
            return user.getName();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
