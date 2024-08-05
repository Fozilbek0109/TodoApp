package tech.uzpro.todoapp.service.impl;

import com.password4j.Hash;
import com.password4j.HashChecker;
import com.password4j.Password;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
//import tech.uzpro.todoapp.config.JwtConfig;
import org.springframework.web.bind.annotation.PostMapping;
import tech.uzpro.todoapp.config.SendMailService;
import tech.uzpro.todoapp.domain.User;
import tech.uzpro.todoapp.model.enums.RoleName;
import tech.uzpro.todoapp.model.payload.auth.LoginDTO;
import tech.uzpro.todoapp.model.payload.auth.RegisterDTO;
import tech.uzpro.todoapp.model.payload.responce.ResponeseDTO;
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
//    private final JwtConfig jwtConfig;

    @Value("${pass.hash.key}")
    private static String passKey;


    public AuthServiceImpl(final UserRepository userRepository, SendMailService sendMailService/*, JwtConfig jwtConfig*/) {
        this.userRepository = userRepository;
        this.sendMailService = sendMailService;
//        this.jwtConfig = jwtConfig;
    }

    @Override
    public ResponeseDTO register(RegisterDTO dto) {

        String passwordHash = passwordHash(dto.getPassword());

        if (!isUsernameValid(dto.getUsername())) {
            return ResponeseDTO.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("Username is invalid")
                    .build();
        }
        if (!isEmailValid(dto.getEmail())) {
            return ResponeseDTO.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("Email is invalid")
                    .build();

        }
        if (!isPasswordValid(dto.getPassword())) {
            return ResponeseDTO.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("Password is invalid")
                    .build();
        }
        if (userRepository.existsByUsernameIgnoreCase(dto.getUsername())) {
            return ResponeseDTO.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("Username is already taken")
                    .build();
        }
        if (userRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            return ResponeseDTO.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("Email is already taken")
                    .build();
        }
        int verificationCode = new Random().nextInt(100000, 999999);
        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordHash)
                .verificationCode(verificationCode)
                .isEnabled(false)
                .roles(Set.of(RoleName.ROLE_USER))
                .build();
        userRepository.save(user);
        sendMailService.sendMail(dto.getEmail(), "UzPro.tech account verification code", "Your verification code is: " + verificationCode);
        return ResponeseDTO.builder()
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .message("Verification code sent")
                .build();
    }

    @Override
    public ResponeseDTO verifyAccount(String email, Integer code) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponeseDTO.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("User not found")
                    .build();
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
            return ResponeseDTO.builder()
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatus.OK.value())
                    .message("Account verified")
                    .build();
        }
        return ResponeseDTO.builder()
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Invalid verification code")
                .build();
    }

    @Override
    public ResponeseDTO login(LoginDTO dto) {
        if (!dto.getEmail().isEmpty() && isEmailValid(dto.getEmail())) {
            Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(dto.getEmail());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                if (passwordHashChecked(dto.getPassword(), user.getPassword())) {
                    if (user.getVerificationCode() != null) {

                        return ResponeseDTO.builder()
                                .status(HttpStatus.OK)
                                .statusCode(HttpStatus.OK.value())
                                .message("User logged in successfully")
                                .build();
//
                    } else {

                        return ResponeseDTO.builder()
                                .status(HttpStatus.NON_AUTHORITATIVE_INFORMATION)
                                .statusCode(HttpStatus.NON_AUTHORITATIVE_INFORMATION.value())
                                .message("User not verification")
                                .build();
                    }
                } else {
                    return ResponeseDTO.builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("Invalid password")
                            .build();
                }
            } else {
                return ResponeseDTO.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .message("Email not found")
                        .build();
            }
        }
        if (!dto.getUsername().isEmpty() && isUsernameValid(dto.getUsername())) {
            Optional<User> optionalUser = userRepository.findByUsernameIgnoreCase(dto.getUsername());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (passwordHashChecked(dto.getPassword(), user.getPassword())) {
                    if (user.getVerificationCode() != null) {
                        return ResponeseDTO.builder()
                                .status(HttpStatus.OK)
                                .statusCode(HttpStatus.OK.value())
                                .message("User logged in successfully")
                                .build();
                    } else {
                        return ResponeseDTO.builder()
                                .status(HttpStatus.NON_AUTHORITATIVE_INFORMATION)
                                .statusCode(HttpStatus.NON_AUTHORITATIVE_INFORMATION.value())
                                .message("User not verification")
                                .build();
                    }
                } else {
                    return ResponeseDTO.builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("Invalid password")
                            .build();
                }
            } else {
              return ResponeseDTO.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .message("User Name not found")
                        .build();
            }
        }
        if (dto.getUsername().isEmpty() && dto.getEmail().isEmpty()) {
            return ResponeseDTO.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("Email or Username is empty")
                    .build();
        }
        return  ResponeseDTO.builder()
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Invalid credentials")
                .build();
    }

    @Override
    public ResponeseDTO forgotPassword(String email) {
        AtomicBoolean isSend = new AtomicBoolean(false);
        userRepository.findByEmailIgnoreCase(email).ifPresent(user -> {
            // send email with new password
            Integer verificationCode = new Random().nextInt(100000, 999999);
            user.setVerificationCode(verificationCode);
            User savedUser = userRepository.save(user);
            if (savedUser.getVerificationCode().equals(verificationCode)) {
                sendMailService.sendMail(email, "UzPro.tech update account password verify code ", "Your verification code is: " + verificationCode);
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
        return isSend.get() ? emailSent : userNotFound;
    }


    @Override
    public ResponeseDTO resetPassword(String email, int code, String password) {
        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getVerificationCode() == code) {
                user.setPassword(password);
                userRepository.save(user);
                return ResponeseDTO.builder()
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Password reset successfully")
                        .build();
            } else {
                return ResponeseDTO.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .message("Invalid verification code")
                        .build();
            }
        }
        return ResponeseDTO.builder()
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("User not found")
                .build();
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

    private static String passwordHash(String password) {
        Hash hash = Password.hash(password)
                .addPepper(passKey)
                .addRandomSalt(32)
                .withArgon2();
        return hash.getResult();
    }

    private static boolean passwordHashChecked(String password, String passwordHash) {
        return Password.check(password, passwordHash)
                .addPepper(passKey)
                .withArgon2();
    }

}
