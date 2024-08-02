package tech.uzpro.todoapp.service;

import org.springframework.http.ResponseEntity;
import tech.uzpro.todoapp.model.payload.auth.LoginDTO;
import tech.uzpro.todoapp.model.payload.auth.RegisterDTO;

public interface AuthService {

    ResponseEntity<?> register(RegisterDTO dto);

    ResponseEntity<?> verifyAccount(String email, Integer code);

    ResponseEntity<?> login(LoginDTO dto);

//    default ResponseEntity<?> refreshToken(String refreshToken) {
//        return null;
//    }

//    default ResponseEntity<?> logout(String refreshToken) {
//        return null;
//    }
//
//    default ResponseEntity<?> logoutAll(String refreshToken) {
//        return null;
//    }

    ResponseEntity<?> forgotPassword(String email);

    ResponseEntity<?> resetPassword(String email,String userName, String password);
}
