package speech_analyzer.backend.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    Long id;

    @NotBlank
    String name;

    @NotBlank
    @Column(unique = true)
    String username;

    @NotBlank
    String password;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Date dateCreated;
}
