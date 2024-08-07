package tech.uzpro.todoapp.rest;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import tech.uzpro.todoapp.model.payload.responce.ResponceTaskDTO;
import tech.uzpro.todoapp.model.payload.responce.ResponceTasksDTO;
import tech.uzpro.todoapp.model.payload.responce.ResponeseDTO;
import tech.uzpro.todoapp.model.payload.tasks.CreatedTaskDTO;
import tech.uzpro.todoapp.model.payload.tasks.GetTaskDTO;
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
    public GetTaskDTO createTask(@RequestBody @Valid CreatedTaskDTO createdTaskDTO) {
        System.out.println("createdTaskDTO = " + createdTaskDTO);
        return taskService.createTask(createdTaskDTO);
    }

    @PostMapping("/update/{id}")
    public ResponeseDTO updateTask(
            @PathVariable("id") UUID id,
            @RequestBody @Valid CreatedTaskDTO createdTaskDTO
    ) {
        return taskService.updateTask(id,createdTaskDTO);
    }

    @PostMapping("/delete/{id}")
    public ResponeseDTO deleteTask(@PathVariable("id") UUID id) {
        return taskService.deleteTask(id);
    }

    @GetMapping("/all")
    public ResponceTasksDTO getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/get/{id}")
    public ResponceTaskDTO getTaskById(@PathVariable("id") UUID id) {
        return taskService.getTaskById(id);
    }

    @GetMapping("/priority/{priority}")
    public ResponceTasksDTO getTasksByPriority(@PathVariable String priority) {
        return taskService.getTasksByPriority(priority);
    }

    @GetMapping("/due-date/{dueDate}")
    public ResponceTasksDTO getTasksByDueDate(@PathVariable Long dueDate) {
        return taskService.getTasksByDueDate(dueDate);
    }

    @GetMapping("/status/{completed}")
    public ResponceTasksDTO getTasksByStatus(@PathVariable boolean completed) {
        return taskService.getTasksByStatus(completed);
    }


}
