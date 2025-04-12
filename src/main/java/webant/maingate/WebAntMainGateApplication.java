package webant.maingate;

import webant.maingate.models.security.User;
import webant.maingate.repositories.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class WebAntMainGateApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebAntMainGateApplication.class, args);
    }

    @Bean
    public ApplicationRunner dataLoader(
            UserRepository repo, PasswordEncoder encoder) {
        return args -> {
            repo.save(
                    new User("habuma", encoder.encode("password"), "user@gmail.com", "+71983942689","ADMIN"));
            repo.save(
                    new User("tacochef", encoder.encode("password"), "user2@gmail.com", "+81983942689","ADMIN"));

        };
    }
}
