package tech.uzpro.todoapp.rest;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.uzpro.todoapp.model.payload.tasks.CreatedTaskDTO;
import tech.uzpro.todoapp.service.TaskService;

import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
public class TaskResource {

private final TaskService taskService;

    public TaskResource(final TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody @Valid CreatedTaskDTO createdTaskDTO) {
        System.out.println("createdTaskDTO = " + createdTaskDTO);
        return taskService.createTask(createdTaskDTO);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateTask(
            @PathVariable("id") UUID id,
            @RequestBody @Valid CreatedTaskDTO createdTaskDTO
    ) {
        return taskService.updateTask(id,createdTaskDTO);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable("id") UUID id) {
        return taskService.deleteTask(id);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable("id") UUID id) {
        return taskService.getTaskById(id);
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<?> getTasksByPriority(@PathVariable String priority) {
        return taskService.getTasksByPriority(priority);
    }

    @GetMapping("/due-date/{dueDate}")
    public ResponseEntity<?> getTasksByDueDate(@PathVariable Long dueDate) {
        return taskService.getTasksByDueDate(dueDate);
    }

    @GetMapping("/status/{completed}")
    public ResponseEntity<?> getTasksByStatus(@PathVariable boolean completed) {
        return taskService.getTasksByStatus(completed);
    }


}
