package tech.uzpro.todoapp.service;

import org.springframework.http.ResponseEntity;
import tech.uzpro.todoapp.model.payload.auth.LoginDTO;
import tech.uzpro.todoapp.model.payload.auth.RegisterDTO;
import tech.uzpro.todoapp.model.payload.responce.ResponeseDTO;

public interface AuthService {

    ResponeseDTO register(RegisterDTO dto);

    ResponeseDTO verifyAccount(String email, Integer code);

    ResponeseDTO login(LoginDTO dto);

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

    ResponeseDTO forgotPassword(String email);

    ResponeseDTO resetPassword(String email,int code, String password);
}
