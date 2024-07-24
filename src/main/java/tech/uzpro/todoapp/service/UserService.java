package tech.uzpro.todoapp.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<?> getMe(HttpServletRequest request);

    ResponseEntity<?> getUserById(Long id);

    ResponseEntity<?> getAllUsers(int page, int limit);

    ResponseEntity<?> updateMyEmail(HttpServletRequest request, String email);

    ResponseEntity<?> updateMyPassword(HttpServletRequest request, String password);

    ResponseEntity<?> updateMyUsername(HttpServletRequest request, String username);

    ResponseEntity<?> deleteUser(HttpServletRequest request);

    ResponseEntity<?> deleteUserById(Long id);

    ResponseEntity<?> getMyTasks(HttpServletRequest request);

    ResponseEntity<?> getUserTasks(Long userId);
}
