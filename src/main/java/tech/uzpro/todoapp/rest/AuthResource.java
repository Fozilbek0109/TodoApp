package tech.uzpro.todoapp.rest;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.uzpro.todoapp.model.payload.auth.LoginDTO;
import tech.uzpro.todoapp.model.payload.auth.RegisterDTO;
import tech.uzpro.todoapp.model.payload.responce.ResponceUserDTO;
import tech.uzpro.todoapp.model.payload.responce.ResponeseDTO;
import tech.uzpro.todoapp.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthResource {
    private final AuthService authService;

    public AuthResource(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponceUserDTO register(@RequestBody @Valid RegisterDTO dto) {
        return authService.register(dto);
    }

    @PostMapping("/verify-account")
    public ResponeseDTO verifyAccount(@RequestParam(name = "e") String email,
                                           @RequestParam(name = "c") Integer code) {
        return authService.verifyAccount(email, code);
    }

    @PostMapping("/login")
    public ResponeseDTO login(@RequestBody @Valid LoginDTO dto) {
        return authService.login(dto);
    }

    @PostMapping("/forgot-password")
    public ResponeseDTO forgotPassword(@RequestParam(name = "e") String email) {
        return authService.forgotPassword(email);
    }

    @PutMapping("/reset-password")
    public ResponeseDTO resetPassword(@RequestParam(name = "e") String email,
                                           @RequestParam(name = "c") int code,
                                           @RequestParam(name = "p") String password) {
        return authService.resetPassword(email, code, password);
    }

//    @PostMapping("/refresh-token")
//    public ResponseEntity<?> refreshToken(@RequestParam(name = "rt") String refreshToken) {
////        return authService.refreshToken(refreshToken);
//    }

}
