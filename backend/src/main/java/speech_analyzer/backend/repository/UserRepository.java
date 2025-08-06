package speech_analyzer.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import speech_analyzer.backend.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    public User findByUsername(String username);
}
