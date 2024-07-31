package tech.uzpro.todoapp.model.payload.responce;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import tech.uzpro.todoapp.domain.Task;

import java.util.Optional;
@Data
@Builder
public class ResponceTaskDTO {
    private HttpStatus status;
    private int statucCode;
    private String message;
    private Optional<Task> task;
}
