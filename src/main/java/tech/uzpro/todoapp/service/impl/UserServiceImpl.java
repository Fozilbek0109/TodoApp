package tech.uzpro.todoapp.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tech.uzpro.todoapp.domain.User;
import tech.uzpro.todoapp.model.payload.responce.ResponceTasksDTO;
import tech.uzpro.todoapp.model.payload.responce.ResponceUserDTO;
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

    public UserServiceImpl(final UserRepository userRepository, final TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public ResponseEntity<?> getMe(HttpServletRequest request) {
        String requestHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            String token = requestHeader.substring(7);
//            TODO: Implement JWT token verification
            if (userRepository.existsByToken(token)) {
                if (userRepository.findByToken(token).isPresent()) {
                    ResponceUserDTO responceUserDTO = ResponceUserDTO.builder().message("User found").status(HttpStatus.OK).statucCode(HttpStatus.OK.value()).user(userRepository.findByToken(token).get()).build();
                    return ResponseEntity.ok(responceUserDTO);
                }
            }
        }
        return null;
    }

    @Override
    public ResponseEntity<?> getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            ResponceUserDTO responceUserDTO = ResponceUserDTO.builder().message("User found").status(HttpStatus.OK).statucCode(HttpStatus.OK.value()).user(optionalUser.get()).build();
            return ResponseEntity.ok(responceUserDTO);
        }
        ResponceUserDTO responceUserDTO = ResponceUserDTO.builder().message("User not found").status(HttpStatus.BAD_REQUEST).statucCode(HttpStatus.BAD_REQUEST.value()).build();
        return ResponseEntity.badRequest().body(responceUserDTO);
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
                ResponceUserDTO responceUserDTO = ResponceUserDTO.builder().message("Email updated").status(HttpStatus.OK).statucCode(HttpStatus.OK.value()).user(userRepository.save(user)).build();
                return ResponseEntity.ok(responceUserDTO);
            }
        }
        ResponceUserDTO responceUserDTO = ResponceUserDTO.builder().message("Unauthorized").status(HttpStatus.UNAUTHORIZED).statucCode(HttpStatus.UNAUTHORIZED.value()).build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responceUserDTO);
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
                ResponceUserDTO responceUserDTO = ResponceUserDTO.builder().message("Password updated").status(HttpStatus.OK).statucCode(HttpStatus.OK.value()).user(userRepository.save(user)).build();
                return ResponseEntity.ok(responceUserDTO);
            }
        }
        ResponceUserDTO responceUserDTO = ResponceUserDTO.builder().message("Unauthorized").status(HttpStatus.UNAUTHORIZED).statucCode(HttpStatus.UNAUTHORIZED.value()).build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responceUserDTO);
    }

    @Override
    public ResponseEntity<?> updateMyUsername(HttpServletRequest request, String username) {
        String tokenByHttpServletRequest = getTokenByHttpServletRequest(request);
        if (tokenByHttpServletRequest != null) {
            User user = getUserByToken(tokenByHttpServletRequest);
            if (user != null) {
                user.setUsername(username);
                ResponceUserDTO responceUserDTO = ResponceUserDTO.builder().message("Username updated").status(HttpStatus.OK).statucCode(HttpStatus.OK.value()).user(userRepository.save(user)).build();
                return ResponseEntity.ok(responceUserDTO);
            }
        }
        ResponceUserDTO responceUserDTO = ResponceUserDTO.builder().message("Unauthorized").status(HttpStatus.UNAUTHORIZED).statucCode(HttpStatus.UNAUTHORIZED.value()).build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responceUserDTO);
    }

    @Override
    public ResponseEntity<?> deleteUser(HttpServletRequest request) {
        String tokenByHttpServletRequest = getTokenByHttpServletRequest(request);
        if (tokenByHttpServletRequest != null) {
            User user = getUserByToken(tokenByHttpServletRequest);
            if (user != null) {
                userRepository.delete(user);
                ResponceUserDTO responceUserDTO = ResponceUserDTO.builder().message("User deleted").status(HttpStatus.OK).statucCode(HttpStatus.OK.value()).build();
                return ResponseEntity.ok(responceUserDTO);
            }
        }
        ResponceUserDTO responceUserDTO = ResponceUserDTO.builder().message("Unauthorized").status(HttpStatus.UNAUTHORIZED).statucCode(HttpStatus.UNAUTHORIZED.value()).build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responceUserDTO);
    }

    @Override
    public ResponseEntity<?> deleteUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            userRepository.delete(optionalUser.get());
            ResponceUserDTO responceUserDTO = ResponceUserDTO.builder().message("User deleted").status(HttpStatus.OK).statucCode(HttpStatus.OK.value()).build();
            return ResponseEntity.ok(responceUserDTO);
        }
        ResponceUserDTO responceUserDTO = ResponceUserDTO.builder().message("User not found").status(HttpStatus.BAD_REQUEST).statucCode(HttpStatus.BAD_REQUEST.value()).build();
        return ResponseEntity.badRequest().body(responceUserDTO);
    }

    @Override
    public ResponseEntity<?> getMyTasks(HttpServletRequest request) {
        String tokenByHttpServletRequest = getTokenByHttpServletRequest(request);
        if (tokenByHttpServletRequest != null) {
            User user = getUserByToken(tokenByHttpServletRequest);
            if (user != null) {
                ResponceTasksDTO responceTasksDTO = ResponceTasksDTO.builder().message("Tasks found").status(HttpStatus.OK).statucCode(HttpStatus.OK.value()).tasks(taskRepository.findAllByUserId(user.getId())).build();
                return ResponseEntity.ok(responceTasksDTO);
            }
        }
        ResponceTasksDTO responceTasksDTO = ResponceTasksDTO.builder().message("Unauthorized").status(HttpStatus.UNAUTHORIZED).statucCode(HttpStatus.UNAUTHORIZED.value()).build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responceTasksDTO);
    }

    @Override
    public ResponseEntity<?> getUserTasks(Long userId) {
        if (!userRepository.existsById(userId)) {
            ResponceTasksDTO responceTasksDTO = ResponceTasksDTO.builder().message("User not found").status(HttpStatus.BAD_REQUEST).statucCode(HttpStatus.BAD_REQUEST.value()).build();
            return ResponseEntity.badRequest().body(responceTasksDTO);
        }
        ResponceTasksDTO responceTasksDTO = ResponceTasksDTO.builder().message("Tasks found").status(HttpStatus.OK).statucCode(HttpStatus.OK.value()).tasks(taskRepository.findAllByUserId(userId)).build();
        return ResponseEntity.ok(responceTasksDTO);
    }

    public User getUserByToken(String token) {
        if (userRepository.existsByToken(token)) {
            if (userRepository.findByToken(token).isPresent())
                return userRepository.findByToken(token).get();
        }
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
