package tech.uzpro.todoapp.service.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tech.uzpro.todoapp.domain.User;
import tech.uzpro.todoapp.model.payload.responce.ResponceTasksDTO;
import tech.uzpro.todoapp.model.payload.responce.ResponceUserDTO;
import tech.uzpro.todoapp.model.payload.responce.ResponeseDTO;
import tech.uzpro.todoapp.repos.TaskRepository;
import tech.uzpro.todoapp.repos.UserRepository;
import tech.uzpro.todoapp.service.UserService;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

import static tech.uzpro.todoapp.model.payload.ResponseEnum.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;


    public UserServiceImpl(final UserRepository userRepository, final TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

/*    @Override
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
*/

    @Override
    public ResponceUserDTO getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return ResponceUserDTO.builder().message(USER_FOUND).status(HttpStatus.OK).statucCode(HttpStatus.OK.value()).user(optionalUser.get()).build();
        }
        return ResponceUserDTO.builder().message(USER_NOT_FOUND).status(HttpStatus.BAD_REQUEST).statucCode(HttpStatus.BAD_REQUEST.value()).build();
    }

    @Override
    public ResponseEntity<?> getAllUsers(int page, int limit) {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("id").descending());
        return ResponseEntity.ok(userRepository.findAll(pageRequest));
    }

    @Override
    public ResponceUserDTO updateMyEmail(Long id, String email) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEmail(email);
            return ResponceUserDTO.builder().message(EMAIL_UPDATE).status(HttpStatus.OK).statucCode(HttpStatus.OK.value()).user(userRepository.save(user)).build();
        }
        return ResponceUserDTO.builder().message(UNAUTHORIZED).status(HttpStatus.UNAUTHORIZED).statucCode(HttpStatus.UNAUTHORIZED.value()).build();
    }

    @Override
    public ResponeseDTO updateMyPassword(long id, String userName, String password) {
        byte[] decode = Base64.getDecoder().decode(password.getBytes(StandardCharsets.UTF_8));
        String decodedPassword = new String(decode, StandardCharsets.UTF_8);
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getUsername().equals(userName)) {
                user.setPassword(decodedPassword);
                return ResponeseDTO.builder().message(PASSWORD_UPDATE).status(HttpStatus.OK).statusCode(HttpStatus.OK.value()).build();
            }
            return ResponeseDTO.builder().message(USER_NAME_NOT_FOUND).status(HttpStatus.BAD_REQUEST).statusCode(HttpStatus.BAD_REQUEST.value()).build();
        }
        return ResponeseDTO.builder().message(UNAUTHORIZED).status(HttpStatus.UNAUTHORIZED).statusCode(HttpStatus.UNAUTHORIZED.value()).build();
    }


    @Override
    public ResponeseDTO deleteUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            userRepository.delete(optionalUser.get());
            return ResponeseDTO.builder().message(USER_DELETED).status(HttpStatus.OK).statusCode(HttpStatus.OK.value()).build();
        }
        return ResponeseDTO.builder().message(USER_NOT_FOUND).status(HttpStatus.BAD_REQUEST).statusCode(HttpStatus.BAD_REQUEST.value()).build();
    }



    @Override
    public ResponceTasksDTO getUserTasks(Long userId) {
        if (!userRepository.existsById(userId)) {
            return ResponceTasksDTO.builder().message(USER_NOT_FOUND).status(HttpStatus.BAD_REQUEST).statucCode(HttpStatus.BAD_REQUEST.value()).build();
        }
        return ResponceTasksDTO.builder().message(TASK_FOUND).status(HttpStatus.OK).statucCode(HttpStatus.OK.value()).tasks(taskRepository.findAllByUserId(userId)).build();
    }


}
