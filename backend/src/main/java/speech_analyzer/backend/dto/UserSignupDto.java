package speech_analyzer.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignupDto
{
    @NotBlank
    private String name;

    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 5, max = 20)
    private String password;
}
