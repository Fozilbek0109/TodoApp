package tech.uzpro.todoapp.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tech.uzpro.todoapp.domain.User;
import tech.uzpro.todoapp.repos.TaskRepository;
import tech.uzpro.todoapp.repos.UserRepository;
import tech.uzpro.todoapp.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public UserServiceImpl(final UserRepository userRepository,final TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public ResponseEntity<?> getMe(HttpServletRequest request) {
        String requestHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            String token = requestHeader.substring(7);
//            TODO: Implement JWT token verification
        }
        return null;
    }

    @Override
    public ResponseEntity<?> getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(optionalUser.get());
        }
        return ResponseEntity.badRequest().body("User not found");
    }

    @Override
    public ResponseEntity<?> getAllUsers(int page, int limit) {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("id").descending());
        return ResponseEntity.ok(userRepository.findAll(pageRequest));
    }

    @Override
    public ResponseEntity<?> updateMyEmail(HttpServletRequest request, String email) {
        String tokenByHttpServletRequest = getTokenByHttpServletRequest(request);
        if (tokenByHttpServletRequest != null) {
            User user = getUserByToken(tokenByHttpServletRequest);
            if (user != null) {
                user.setEmail(email);
                return ResponseEntity.ok(userRepository.save(user));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }

    @Override
    public ResponseEntity<?> updateMyPassword(HttpServletRequest request, String password) {
        byte[] decode = Base64.getDecoder().decode(password.getBytes(StandardCharsets.UTF_8));
        String decodedPassword = new String(decode, StandardCharsets.UTF_8);
        String tokenByHttpServletRequest = getTokenByHttpServletRequest(request);
        if (tokenByHttpServletRequest != null) {
            User user = getUserByToken(tokenByHttpServletRequest);
            if (user != null) {
                user.setPassword(decodedPassword);
                return ResponseEntity.ok(userRepository.save(user));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }

    @Override
    public ResponseEntity<?> updateMyUsername(HttpServletRequest request, String username) {
        String tokenByHttpServletRequest = getTokenByHttpServletRequest(request);
        if (tokenByHttpServletRequest != null) {
            User user = getUserByToken(tokenByHttpServletRequest);
            if (user != null) {
                user.setUsername(username);
                return ResponseEntity.ok(userRepository.save(user));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }

    @Override
    public ResponseEntity<?> deleteUser(HttpServletRequest request) {
        String tokenByHttpServletRequest = getTokenByHttpServletRequest(request);
        if (tokenByHttpServletRequest != null) {
            User user = getUserByToken(tokenByHttpServletRequest);
            if (user != null) {
                userRepository.delete(user);
                return ResponseEntity.ok("User deleted");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }

    @Override
    public ResponseEntity<?> deleteUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            userRepository.delete(optionalUser.get());
            return ResponseEntity.ok("User deleted");
        }
        return ResponseEntity.badRequest().body("User not found");
    }

    @Override
    public ResponseEntity<?> getMyTasks(HttpServletRequest request) {
        String tokenByHttpServletRequest = getTokenByHttpServletRequest(request);
        if (tokenByHttpServletRequest != null) {
            User user = getUserByToken(tokenByHttpServletRequest);
            if (user != null) {
                return ResponseEntity.ok(taskRepository.findAllByUserId(user.getId()));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }

    @Override
    public ResponseEntity<?> getUserTasks(Long userId) {
        return ResponseEntity.ok(taskRepository.findAllByUserId(userId));
    }

    public User getUserByToken(String token) {
//        TODO: Implement JWT token verification
        return null;
    }

    private String getTokenByHttpServletRequest(HttpServletRequest request) {
        String requestHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            return requestHeader.substring(7);
        }
        return null;
    }
}
