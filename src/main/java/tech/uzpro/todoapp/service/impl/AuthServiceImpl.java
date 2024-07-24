package tech.uzpro.todoapp.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tech.uzpro.todoapp.config.SendMailService;
import tech.uzpro.todoapp.domain.User;
import tech.uzpro.todoapp.model.enums.RoleName;
import tech.uzpro.todoapp.model.payload.auth.LoginDTO;
import tech.uzpro.todoapp.model.payload.auth.RegisterDTO;
import tech.uzpro.todoapp.repos.UserRepository;
import tech.uzpro.todoapp.service.AuthService;

import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final SendMailService sendMailService;

    public AuthServiceImpl(final UserRepository userRepository, SendMailService sendMailService) {
        this.userRepository = userRepository;
        this.sendMailService = sendMailService;
    }

    @Override
    public ResponseEntity<?> register(RegisterDTO dto) {
        if (!isUsernameValid(dto.getUsername())) {
            return ResponseEntity.badRequest().body("Username is invalid");
        }
        if (!isEmailValid(dto.getEmail())) {
            return ResponseEntity.badRequest().body("Email is invalid");
        }
        if (!isPasswordValid(dto.getPassword())) {
            return ResponseEntity.badRequest().body("Password is invalid");
        }
        if (userRepository.existsByUsernameIgnoreCase(dto.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }
        if (userRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already taken");
        }
        Integer verificationCode = new Random().nextInt(100000, 999999);
        User build = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .verificationCode(verificationCode)
                .roles(Set.of(RoleName.ROLE_USER))
                .isEnabled(false)
                .build();
        sendMailService.sendMail(dto.getEmail(), "UzPro.tech account verification code", "Your verification code is: " + verificationCode);
        return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(build));
    }

    @Override
    public ResponseEntity<?> verifyAccount(String email, Integer code) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        User user = optionalUser.get();
        if (user.getVerificationCode().equals(code)) {
            user.setVerificationCode(null);
            user.setEnabled(true);
            userRepository.save(user);
            sendMailService.sendMail(
                    user.getEmail(),
                    "UzPro.tech account verified",
                    "<b>Your account has been verified successfully</b>\nThank you for using UzPro.tech\n\n<a href='https://uzpro.tech'>UzPro.tech</a>"
                    );
            return ResponseEntity.ok("Account verified successfully");
        }
        return null;
    }

    @Override
    public ResponseEntity<?> login(LoginDTO dto) {
        if (isEmailValid(dto.getEmail())) {
            Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(dto.getEmail());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (user.getPassword().equals(dto.getPassword())) {
                    return ResponseEntity.ok(user);
                }
            } else {
                return ResponseEntity.badRequest().body("User not found");
            }
        } else if (isUsernameValid(dto.getUsername())) {
            Optional<User> optionalUser = userRepository.findByUsernameIgnoreCase(dto.getUsername());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (user.getPassword().equals(dto.getPassword())) {
                    return ResponseEntity.ok(user);
                }
            } else {
                return ResponseEntity.badRequest().body("User not found");
            }
        }
        return ResponseEntity.badRequest().body("Invalid credentials");
    }

    @Override
    public ResponseEntity<?> forgotPassword(String email) {
        AtomicBoolean isSend = new AtomicBoolean(false);
        userRepository.findByEmailIgnoreCase(email).ifPresent(user -> {
            // send email with new password
            Integer verificationCode = new Random().nextInt(100000, 999999);
            user.setVerificationCode(verificationCode);
            User savedUser = userRepository.save(user);
            if (savedUser.getVerificationCode().equals(verificationCode)) {
                // TODO: send email
                isSend.set(true);
            } else {
                throw new RuntimeException("An unknown error occurred on the server: verification code mismatch");
            }
        });
        return isSend.get() ? ResponseEntity.ok("Email sent") : ResponseEntity.badRequest().body("User not found");
    }

    @Override
    public ResponseEntity<?> resetPassword(String email, String token, String password) {
        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getVerificationCode().equals(Integer.parseInt(token))) {
                user.setPassword(password);
                userRepository.save(user);
                return ResponseEntity.ok("Password reset successfully");
            } else {
                return ResponseEntity.badRequest().body("Invalid token");
            }
        }
        return ResponseEntity.badRequest().body("User not found");
    }

    private static boolean isEmailValid(String email) {
        return email.matches("^(.+)@(.+)$");
    }

    private static boolean isPasswordValid(String password) {
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$");
    }

    private static boolean isUsernameValid(String username) {
        return username.matches("^[a-zA-Z0-9]{3,40}$");
    }
}
