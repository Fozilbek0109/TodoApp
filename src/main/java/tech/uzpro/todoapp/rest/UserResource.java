package tech.uzpro.todoapp.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.uzpro.todoapp.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserResource {

    private final UserService userService;

    public UserResource(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return userService.getAllUsers(page, size);
    }

    //    u -> username
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserByUsername(@PathVariable Long id) {
        return userService.getUserById(id);
    }

//    @GetMapping("/me")
//    public ResponseEntity<?> getMe(HttpServletRequest request) {
//        return ResponseEntity.ok(userService.getMe(request));
//    }

//    @GetMapping("/me/tasks")
//    public ResponseEntity<?> getMyTasks(HttpServletRequest request) {
//        return ResponseEntity.ok(userService.getMyTasks(request));
//    }

    @GetMapping("/{userId}/tasks")
    public ResponseEntity<?> getUserTasks(@PathVariable Long userId) {
        return userService.getUserTasks(userId);
    }

    @PutMapping("/me/email")
    public ResponseEntity<?> updateMyEmail(@RequestParam Long id, @RequestParam String email) {
        return userService.updateMyEmail(id, email);
    }

    @PutMapping("/me/password")
    public ResponseEntity<?> updateMyPassword(
            @RequestParam Long id,
            @RequestParam String email,
            @RequestParam String password) {
        return userService.updateMyPassword(id, email, password);
    }

//    @PutMapping("/me/username")
//    public ResponseEntity<?> updateMyUsername(HttpServletRequest request, @RequestParam String username) {
//        return userService.updateMyUsername(request, username);
//    }

//    @DeleteMapping("/me")
//    public ResponseEntity<?> deleteUser(HttpServletRequest request) {
//        return userService.deleteUser(request);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        return userService.deleteUserById(id);
    }
}
