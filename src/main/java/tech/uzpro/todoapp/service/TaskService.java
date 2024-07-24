package tech.uzpro.todoapp.service;

import org.springframework.http.ResponseEntity;
import tech.uzpro.todoapp.model.payload.tasks.CreatedTaskDTO;
import tech.uzpro.todoapp.model.payload.tasks.GetTaskDTO;

import java.util.UUID;

public interface TaskService {
    ResponseEntity<?> createTask(CreatedTaskDTO createdTaskDTO);

    ResponseEntity<?> updateTask(UUID id, CreatedTaskDTO createdTaskDTO);

    ResponseEntity<?> deleteTask(UUID id);

    ResponseEntity<?> getAllTasks();

    ResponseEntity<?> getTaskById(UUID id);

    ResponseEntity<?> getTasksByPriority(String priority);

    ResponseEntity<?> getTasksByDueDate(Long dueDate);

    ResponseEntity<?> getTasksByStatus(boolean completed);
}
