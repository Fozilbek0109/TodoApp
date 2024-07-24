package tech.uzpro.todoapp.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tech.uzpro.todoapp.domain.Task;
import tech.uzpro.todoapp.model.enums.Priority;
import tech.uzpro.todoapp.model.payload.tasks.CreatedTaskDTO;
import tech.uzpro.todoapp.repos.TaskRepository;
import tech.uzpro.todoapp.service.TaskService;

import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(final TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public ResponseEntity<?> createTask(CreatedTaskDTO createdTaskDTO) {
        Task task = Task.builder()
                .title(createdTaskDTO.getTitle())
                .description(createdTaskDTO.getDescription())
                .priority(Priority.valueOf(createdTaskDTO.getPriority()))
                .dueDate(createdTaskDTO.getDueDate())
                .isCompleted(createdTaskDTO.isCompleted())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(taskRepository.save(task));
    }

    @Override
    public ResponseEntity<?> updateTask(UUID id, CreatedTaskDTO createdTaskDTO) {
        if (!taskRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("Task not found");
        }
        Task task = Task.builder()
                .id(id)
                .title(createdTaskDTO.getTitle())
                .description(createdTaskDTO.getDescription())
                .priority(Priority.valueOf(createdTaskDTO.getPriority()))
                .dueDate(createdTaskDTO.getDueDate())
                .isCompleted(createdTaskDTO.isCompleted())
                .build();
        return ResponseEntity.status(HttpStatus.UPGRADE_REQUIRED).body(taskRepository.save(task));
    }

    @Override
    public ResponseEntity<?> deleteTask(UUID id) {
        if (!taskRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("Task not found");
        }
        taskRepository.deleteById(id);
        return ResponseEntity.ok("Task deleted");
    }

    @Override
    public ResponseEntity<?> getAllTasks() {
        if (taskRepository.findAll().isEmpty()) {
            return ResponseEntity.badRequest().body("No tasks found");
        }
        return ResponseEntity.ok(taskRepository.findAll());
    }

    @Override
    public ResponseEntity<?> getTaskById(UUID id) {
        if (taskRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("Task not found");
        }
        return ResponseEntity.ok(taskRepository.findById(id));
    }

    @Override
    public ResponseEntity<?> getTasksByPriority(String priority) {
        if (taskRepository.findAllByPriority(Priority.valueOf(priority)).isEmpty()) {
            return ResponseEntity.badRequest().body("No tasks found");
        }
        return ResponseEntity.ok(taskRepository.findAllByPriority(Priority.valueOf(priority)));
    }

    @Override
    public ResponseEntity<?> getTasksByDueDate(Long dueDate) {
        if (taskRepository.findAllByDueDate(dueDate).isEmpty()) {
            return ResponseEntity.badRequest().body("No tasks found");
        }
        return ResponseEntity.ok(taskRepository.findAllByDueDate(dueDate));
    }

    @Override
    public ResponseEntity<?> getTasksByStatus(boolean completed) {
        if (taskRepository.findAllByIsCompleted(completed).isEmpty()) {
            return ResponseEntity.badRequest().body("No tasks found");
        }
        return ResponseEntity.ok(taskRepository.findAllByIsCompleted(completed));
    }
}
