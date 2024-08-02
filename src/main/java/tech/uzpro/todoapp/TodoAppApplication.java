package tech.uzpro.todoapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tech.uzpro.todoapp.domain.User;
import tech.uzpro.todoapp.model.enums.RoleName;
import tech.uzpro.todoapp.repos.UserRepository;

import java.util.Set;

import static tech.uzpro.todoapp.model.enums.RoleName.*;

@SpringBootApplication
public class TodoAppApplication implements CommandLineRunner {
    private final UserRepository userRepository;

    @Autowired
    public TodoAppApplication(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(TodoAppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        User.builder()
                .isEnabled(true)
                .username("admin")
                .email("admin@uzpro.tech")
                .password("$2a$10$3")
                .roles(
                        Set.of(ROLE_ADMIN, ROLE_USER)
                )
                .build();
    }
}
