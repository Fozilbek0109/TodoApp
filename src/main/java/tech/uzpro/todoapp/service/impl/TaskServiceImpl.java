package tech.uzpro.todoapp.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tech.uzpro.todoapp.domain.Task;
import tech.uzpro.todoapp.domain.User;
import tech.uzpro.todoapp.model.enums.Priority;
import tech.uzpro.todoapp.model.payload.responce.ResponceTaskDTO;
import tech.uzpro.todoapp.model.payload.responce.ResponceTasksDTO;
import tech.uzpro.todoapp.model.payload.responce.ResponeseDTO;
import tech.uzpro.todoapp.model.payload.tasks.CreatedTaskDTO;
import tech.uzpro.todoapp.repos.TaskRepository;
import tech.uzpro.todoapp.repos.UserRepository;
import tech.uzpro.todoapp.service.TaskService;

import java.util.Optional;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskServiceImpl(final TaskRepository taskRepository, final UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<?> createTask(CreatedTaskDTO createdTaskDTO) {
        Optional<User> optionalUser = userRepository.findById(createdTaskDTO.getUserId());
        if (optionalUser.isEmpty()) {
            ResponeseDTO responeseDto = ResponeseDTO.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("User not found")
                    .build();
            return ResponseEntity.badRequest().body(responeseDto);
        }
        Task task = Task.builder()
                .title(createdTaskDTO.getTitle())
                .description(createdTaskDTO.getDescription())
                .priority(Priority.valueOf(createdTaskDTO.getPriority()))
                .dueDate(createdTaskDTO.getDueDate())
                .isCompleted(createdTaskDTO.isCompleted())
                .user(optionalUser.get())
                .build();
        ResponeseDTO responeseDto = ResponeseDTO.builder()
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .message("Task created")
                .build();
        taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(responeseDto);
    }

    @Override
    public ResponseEntity<?> updateTask(UUID id, CreatedTaskDTO createdTaskDTO) {
        if (!taskRepository.existsById(id)) {
            ResponeseDTO responeseDto = ResponeseDTO.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("Task not found")
                    .build();
            return ResponseEntity.badRequest().body(responeseDto);
        }
        Task task = Task.builder()
                .id(id)
                .title(createdTaskDTO.getTitle())
                .description(createdTaskDTO.getDescription())
                .priority(Priority.valueOf(createdTaskDTO.getPriority()))
                .dueDate(createdTaskDTO.getDueDate())
                .isCompleted(createdTaskDTO.isCompleted())
                .build();
        ResponeseDTO responeseDto = ResponeseDTO.builder()
                .status(HttpStatus.UPGRADE_REQUIRED)
                .statusCode(HttpStatus.UPGRADE_REQUIRED.value())
                .message("Task updated")
                .build();
        taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.UPGRADE_REQUIRED).body(responeseDto);
    }

    @Override
    public ResponseEntity<?> deleteTask(UUID id) {
        if (!taskRepository.existsById(id)) {
            ResponeseDTO responeseDto = ResponeseDTO.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("Task not found")
                    .build();
            return ResponseEntity.badRequest().body(responeseDto);
        }
        taskRepository.deleteById(id);
        ResponeseDTO responeseDto = ResponeseDTO.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Task deleted")
                .build();
        return ResponseEntity.ok(responeseDto);
    }

    @Override
    public ResponseEntity<?> getAllTasks() {
        if (taskRepository.findAll().isEmpty()) {
            ResponceTasksDTO responceTasksDTO = ResponceTasksDTO.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .statucCode(HttpStatus.BAD_REQUEST.value())
                    .message("No tasks found")
                    .tasks(null)
                    .build();
            return ResponseEntity.badRequest().body(responceTasksDTO);
        }
        ResponceTasksDTO responceTasksDTO = ResponceTasksDTO.builder()
                .status(HttpStatus.OK)
                .statucCode(HttpStatus.OK.value())
                .message("Tasks found")
                .tasks(taskRepository.findAll())
                .build();
        return ResponseEntity.ok(responceTasksDTO);
    }

    @Override
    public ResponseEntity<?> getTaskById(UUID id) {
        if (taskRepository.existsById(id)) {
            ResponceTaskDTO responceTaskDTO = ResponceTaskDTO.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .statucCode(HttpStatus.BAD_REQUEST.value())
                    .message("Task not found")
                    .task(java.util.Optional.empty())
                    .build();
            return ResponseEntity.badRequest().body(responceTaskDTO);
        }
        ResponceTaskDTO responceTaskDTO = ResponceTaskDTO.builder()
                .status(HttpStatus.OK)
                .statucCode(HttpStatus.OK.value())
                .message("Task found")
                .task(taskRepository.findById(id))
                .build();
        return ResponseEntity.ok(responceTaskDTO);
    }

    @Override
    public ResponseEntity<?> getTasksByPriority(String priority) {
        if (taskRepository.findAllByPriority(Priority.valueOf(priority)).isEmpty()) {
            ResponceTasksDTO responceTasksDTO = ResponceTasksDTO.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .statucCode(HttpStatus.BAD_REQUEST.value())
                    .message("No tasks found")
                    .tasks(null)
                    .build();
            return ResponseEntity.badRequest().body(responceTasksDTO);
        }
        ResponceTasksDTO responceTasksDTO = ResponceTasksDTO.builder()
                .status(HttpStatus.OK)
                .statucCode(HttpStatus.OK.value())
                .message("Tasks found")
                .tasks(taskRepository.findAllByPriority(Priority.valueOf(priority)))
                .build();
        return ResponseEntity.ok(responceTasksDTO);
    }

    @Override
    public ResponseEntity<?> getTasksByDueDate(Long dueDate) {
        if (taskRepository.findAllByDueDate(dueDate).isEmpty()) {
            ResponceTasksDTO responceTasksDTO = ResponceTasksDTO.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .statucCode(HttpStatus.BAD_REQUEST.value())
                    .message("No tasks found")
                    .tasks(null)
                    .build();
            return ResponseEntity.badRequest().body(responceTasksDTO);
        }
        ResponceTasksDTO responceTasksDTO = ResponceTasksDTO.builder()
                .status(HttpStatus.OK)
                .statucCode(HttpStatus.OK.value())
                .message("Tasks found")
                .tasks(taskRepository.findAllByDueDate(dueDate))
                .build();
        return ResponseEntity.ok(responceTasksDTO);
    }

    @Override
    public ResponseEntity<?> getTasksByStatus(boolean completed) {
        if (taskRepository.findAllByIsCompleted(completed).isEmpty()) {
            ResponceTasksDTO responceTasksDTO = ResponceTasksDTO.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .statucCode(HttpStatus.BAD_REQUEST.value())
                    .message("No tasks found")
                    .tasks(null)
                    .build();
            return ResponseEntity.badRequest().body(responceTasksDTO);
        }
        ResponceTasksDTO responceTasksDTO = ResponceTasksDTO.builder()
                .status(HttpStatus.OK)
                .statucCode(HttpStatus.OK.value())
                .message("Tasks found")
                .tasks(taskRepository.findAllByIsCompleted(completed))
                .build();
        return ResponseEntity.ok(responceTasksDTO);
    }
}
