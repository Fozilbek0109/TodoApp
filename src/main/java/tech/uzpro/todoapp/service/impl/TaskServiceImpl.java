package tech.uzpro.todoapp.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tech.uzpro.todoapp.domain.Task;
import tech.uzpro.todoapp.domain.User;
import tech.uzpro.todoapp.model.enums.Priority;
import tech.uzpro.todoapp.model.payload.responce.ResponceTaskDTO;
import tech.uzpro.todoapp.model.payload.responce.ResponceTasksDTO;
import tech.uzpro.todoapp.model.payload.responce.ResponeseDTO;
import tech.uzpro.todoapp.model.payload.tasks.CreatedTaskDTO;
import tech.uzpro.todoapp.model.payload.tasks.GetTaskDTO;
import tech.uzpro.todoapp.repos.TaskRepository;
import tech.uzpro.todoapp.repos.UserRepository;
import tech.uzpro.todoapp.service.TaskService;

import java.util.Optional;
import java.util.UUID;

import static tech.uzpro.todoapp.model.payload.ResponseEnum.*;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskServiceImpl(final TaskRepository taskRepository, final UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public GetTaskDTO createTask(CreatedTaskDTO createdTaskDTO) {
        Optional<User> optionalUser = userRepository.findById(createdTaskDTO.getUserId());
        if (optionalUser.isEmpty()) {
            return GetTaskDTO.builder()
                    .id(null)
                    .title(null)
                    .description(null)
                    .priority(null)
                    .dueDate(null)
                    .completed(false).build();
        }
        Task task = Task.builder()
                .title(createdTaskDTO.getTitle())
                .description(createdTaskDTO.getDescription())
                .priority(Priority.valueOf(createdTaskDTO.getPriority()))
                .dueDate(createdTaskDTO.getDueDate())
                .isCompleted(createdTaskDTO.isCompleted())
                .user(optionalUser.get())
                .build();
        taskRepository.save(task);
        return GetTaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .priority(task.getPriority().name())
                .dueDate(task.getDueDate())
                .completed(task.isCompleted()).build();
    }

    @Override
    public ResponeseDTO updateTask(UUID id, CreatedTaskDTO createdTaskDTO) {
        if (!taskRepository.existsById(id)) {
            return ResponeseDTO.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message(TASK_NOT_FOUND)
                    .build();
        }
        Task task = Task.builder()
                .id(id)
                .title(createdTaskDTO.getTitle())
                .description(createdTaskDTO.getDescription())
                .priority(Priority.valueOf(createdTaskDTO.getPriority()))
                .dueDate(createdTaskDTO.getDueDate())
                .isCompleted(createdTaskDTO.isCompleted())
                .build();
        taskRepository.save(task);
        return ResponeseDTO.builder()
                .status(HttpStatus.UPGRADE_REQUIRED)
                .statusCode(HttpStatus.UPGRADE_REQUIRED.value())
                .message(TASK_UPDATED)
                .build();
    }

    @Override
    public ResponeseDTO deleteTask(UUID id) {
        if (!taskRepository.existsById(id)) {
            return ResponeseDTO.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message(TASK_NOT_FOUND)
                    .build();
        }
        taskRepository.deleteById(id);
        return ResponeseDTO.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message(TASK_DELETED)
                .build();
    }

    @Override
    public ResponceTasksDTO getAllTasks() {
        if (taskRepository.findAll().isEmpty()) {
            return ResponceTasksDTO.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .statucCode(HttpStatus.BAD_REQUEST.value())
                    .message(NO_TASK_FOUND)
                    .tasks(null)
                    .build();
        }
        return ResponceTasksDTO.builder()
                .status(HttpStatus.OK)
                .statucCode(HttpStatus.OK.value())
                .message(TASK_FOUND)
                .tasks(taskRepository.findAll())
                .build();
    }

    @Override
    public ResponceTaskDTO getTaskById(UUID id) {
        if (!taskRepository.existsById(id)) {
            return ResponceTaskDTO.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .statucCode(HttpStatus.BAD_REQUEST.value())
                    .message(TASK_NOT_FOUND)
                    .task(java.util.Optional.empty())
                    .build();
        }
        return ResponceTaskDTO.builder()
                .status(HttpStatus.OK)
                .statucCode(HttpStatus.OK.value())
                .message(TASK_FOUND)
                .task(taskRepository.findById(id))
                .build();
    }

    @Override
    public ResponceTasksDTO getTasksByPriority(String priority) {
        if (taskRepository.findAllByPriority(Priority.valueOf(priority)).isEmpty()) {
            return ResponceTasksDTO.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .statucCode(HttpStatus.BAD_REQUEST.value())
                    .message(NO_TASK_FOUND)
                    .tasks(null)
                    .build();
        }
        return ResponceTasksDTO.builder()
                .status(HttpStatus.OK)
                .statucCode(HttpStatus.OK.value())
                .message(TASK_FOUND)
                .tasks(taskRepository.findAllByPriority(Priority.valueOf(priority)))
                .build();
    }

    @Override
    public ResponceTasksDTO getTasksByDueDate(Long dueDate) {
        if (taskRepository.findAllByDueDate(dueDate).isEmpty()) {
            return ResponceTasksDTO.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .statucCode(HttpStatus.BAD_REQUEST.value())
                    .message(NO_TASK_FOUND)
                    .tasks(null)
                    .build();
        }
        return ResponceTasksDTO.builder()
                .status(HttpStatus.OK)
                .statucCode(HttpStatus.OK.value())
                .message(TASK_FOUND)
                .tasks(taskRepository.findAllByDueDate(dueDate))
                .build();
    }

    @Override
    public ResponceTasksDTO getTasksByStatus(boolean completed) {
        if (taskRepository.findAllByIsCompleted(completed).isEmpty()) {
            return ResponceTasksDTO.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .statucCode(HttpStatus.BAD_REQUEST.value())
                    .message(NO_TASK_FOUND)
                    .tasks(null)
                    .build();
        }
        return ResponceTasksDTO.builder()
                .status(HttpStatus.OK)
                .statucCode(HttpStatus.OK.value())
                .message(TASK_FOUND)
                .tasks(taskRepository.findAllByIsCompleted(completed))
                .build();
    }
}
