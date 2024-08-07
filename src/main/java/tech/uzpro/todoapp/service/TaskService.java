package tech.uzpro.todoapp.service;

import org.springframework.http.ResponseEntity;
import tech.uzpro.todoapp.model.payload.responce.ResponceTaskDTO;
import tech.uzpro.todoapp.model.payload.responce.ResponceTasksDTO;
import tech.uzpro.todoapp.model.payload.responce.ResponeseDTO;
import tech.uzpro.todoapp.model.payload.tasks.CreatedTaskDTO;
import tech.uzpro.todoapp.model.payload.tasks.GetTaskDTO;

import java.util.UUID;

public interface TaskService {
    ResponceTaskDTO createTask(CreatedTaskDTO createdTaskDTO);

    ResponceTaskDTO updateTask(UUID id, CreatedTaskDTO createdTaskDTO);

    ResponeseDTO deleteTask(UUID id);

    ResponceTasksDTO getAllTasks();

    ResponceTaskDTO getTaskById(UUID id);

    ResponceTasksDTO getTasksByPriority(String priority);

    ResponceTasksDTO getTasksByDueDate(Long dueDate);

    ResponceTasksDTO getTasksByStatus(boolean completed);
}
