package tech.uzpro.todoapp.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tech.uzpro.todoapp.config.JwtConfig;
import tech.uzpro.todoapp.config.SendMailService;
import tech.uzpro.todoapp.domain.User;
import tech.uzpro.todoapp.model.payload.auth.LoginDTO;
import tech.uzpro.todoapp.model.payload.auth.RegisterDTO;
import tech.uzpro.todoapp.model.payload.responce.ResponeseDTO;
import tech.uzpro.todoapp.repos.UserRepository;
import tech.uzpro.todoapp.service.AuthService;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final SendMailService sendMailService;
    private final JwtConfig jwtConfig;
    public AuthServiceImpl(final UserRepository userRepository, SendMailService sendMailService, JwtConfig jwtConfig) {
        this.userRepository = userRepository;
        this.sendMailService = sendMailService;
        this.jwtConfig = jwtConfig;
    }

    @Override
    public ResponseEntity<?> register(RegisterDTO dto) {
        if (!isUsernameValid(dto.getUsername())) {
            ResponeseDTO usernameIsInvalid = ResponeseDTO.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("Username is invalid")
                    .build();
            return ResponseEntity.badRequest().body(usernameIsInvalid);
        }
        if (!isEmailValid(dto.getEmail())) {
            ResponeseDTO emailIsInvalid = ResponeseDTO.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("Email is invalid")
                    .build();
            return ResponseEntity.badRequest().body(emailIsInvalid);
        }
        if (!isPasswordValid(dto.getPassword())) {
            ResponeseDTO passwordIsInvalid = ResponeseDTO.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("Password is invalid")
                    .build();
            return ResponseEntity.badRequest().body(passwordIsInvalid);
        }
        if (userRepository.existsByUsernameIgnoreCase(dto.getUsername())) {
            ResponeseDTO usernameIsAlreadyTaken = ResponeseDTO.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("Username is already taken")
                    .build();
            return ResponseEntity.badRequest().body(usernameIsAlreadyTaken);
        }
        if (userRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            ResponeseDTO emailIsAlreadyTaken = ResponeseDTO.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("Email is already taken")
                    .build();
            return ResponseEntity.badRequest().body(emailIsAlreadyTaken);
        }
        int verificationCode = new Random().nextInt(100000, 999999);
        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .verificationCode(verificationCode)
                .build();
        userRepository.save(user);
        ResponeseDTO responeseDto = ResponeseDTO.builder()
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .message("Verification code sent")
                .build();
        sendMailService.sendMail(dto.getEmail(), "UzPro.tech account verification code", "Your verification code is: " + verificationCode);
        return ResponseEntity.status(HttpStatus.CREATED).body(responeseDto);
    }

    @Override
    public ResponseEntity<?> verifyAccount(String email, Integer code) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            ResponeseDTO userNotFound = ResponeseDTO.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("User not found")
                    .build();
            return ResponseEntity.badRequest().body(userNotFound);
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
            ResponeseDTO accountVerified = ResponeseDTO.builder()
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatus.OK.value())
                    .message("Account verified")
                    .build();
            return ResponseEntity.ok(accountVerified);
        }
        ResponeseDTO invalidVerificationCode = ResponeseDTO.builder()
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Invalid verification code")
                .build();
        return ResponseEntity.ok(invalidVerificationCode);
    }

    @Override
    public ResponseEntity<?> login(LoginDTO dto) {
        if (!dto.getEmail().isEmpty()&&isEmailValid(dto.getEmail())) {
            Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(dto.getEmail());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (user.getPassword().equals(dto.getPassword())) {
                    ResponeseDTO userLoggedInSuccessfully = ResponeseDTO.builder()
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .message("User logged in successfully")
                            .build();
//                    TODO: generate token

                    return ResponseEntity.ok(userLoggedInSuccessfully);
                }else {
                    ResponeseDTO invalidPassword = ResponeseDTO.builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("Invalid password")
                            .build();
                    return ResponseEntity.badRequest().body(invalidPassword);
                }
            } else {
                ResponeseDTO userNotFound = ResponeseDTO.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .message("Email not found")
                        .build();
                return ResponseEntity.badRequest().body(userNotFound);
            }
        }
        if (!dto.getUsername().isEmpty()&&isUsernameValid(dto.getUsername())) {
            Optional<User> optionalUser = userRepository.findByUsernameIgnoreCase(dto.getUsername());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (user.getPassword().equals(dto.getPassword())) {
                    ResponeseDTO userLoggedInSuccessfully = ResponeseDTO.builder()
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .message("User logged in successfully")
                            .build();
                    return ResponseEntity.ok(userLoggedInSuccessfully);
                }else {
                    ResponeseDTO invalidPassword = ResponeseDTO.builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("Invalid password")
                            .build();
                    return ResponseEntity.badRequest().body(invalidPassword);
                }
            } else {
                ResponeseDTO userNotFound = ResponeseDTO.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .message("User Name not found")
                        .build();
                return ResponseEntity.badRequest().body(userNotFound);
            }
        }
        if (dto.getUsername().isEmpty() && dto.getEmail().isEmpty()) {
            ResponeseDTO emptyCredentials = ResponeseDTO.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("Email or Username is empty")
                    .build();
            return ResponseEntity.badRequest().body(emptyCredentials);
        }
        ResponeseDTO invalidCredentials = ResponeseDTO.builder()
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Invalid credentials")
                .build();
        return ResponseEntity.badRequest().body(invalidCredentials);
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
        ResponeseDTO emailSent = ResponeseDTO.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Email sent")
                .build();
        ResponeseDTO userNotFound = ResponeseDTO.builder()
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("User not found")
                .build();
        return isSend.get() ? ResponseEntity.ok(emailSent) : ResponseEntity.badRequest().body(userNotFound);
    }

    @Override
    public ResponseEntity<?> resetPassword(String email, String token, String password) {
        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getVerificationCode().equals(Integer.parseInt(token))) {
                user.setPassword(password);
                userRepository.save(user);
                ResponeseDTO passwordResetSuccessfully = ResponeseDTO.builder()
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Password reset successfully")
                        .build();
                return ResponseEntity.ok(passwordResetSuccessfully);
            } else {
                ResponeseDTO invalidToken = ResponeseDTO.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .message("Invalid token")
                        .build();
                return ResponseEntity.badRequest().body(invalidToken);
            }
        }
        ResponeseDTO userNotFound = ResponeseDTO.builder()
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("User not found")
                .build();
        return ResponseEntity.badRequest().body(userNotFound);
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
