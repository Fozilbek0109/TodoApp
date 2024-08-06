package tech.uzpro.todoapp.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import tech.uzpro.todoapp.model.payload.responce.ResponceTasksDTO;
import tech.uzpro.todoapp.model.payload.responce.ResponceUserDTO;
import tech.uzpro.todoapp.model.payload.responce.ResponeseDTO;

public interface UserService {

//    ResponseEntity<?> getMe(HttpServletRequest request);

    ResponceUserDTO getUserById(Long id);

    ResponseEntity<?> getAllUsers(int page, int limit);

    ResponceUserDTO updateMyEmail(Long id, String email);

    ResponeseDTO updateMyPassword(long id, String email, String password);

//    ResponseEntity<?> updateMyUsername(HttpServletRequest request, String username);

//    ResponseEntity<?> deleteUser(HttpServletRequest request);

    ResponeseDTO deleteUserById(Long id);

//    ResponseEntity<?> getMyTasks(HttpServletRequest request);

    ResponceTasksDTO getUserTasks(Long userId);

    ResponceUserDTO getUserByEmail(String email);
}
