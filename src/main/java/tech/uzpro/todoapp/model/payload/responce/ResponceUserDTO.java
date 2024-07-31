package tech.uzpro.todoapp.model.payload.responce;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import tech.uzpro.todoapp.domain.User;

@Data
@Builder
public class ResponceUserDTO {
    private HttpStatus status;
    private int statucCode;
    private String message;
    private User user;
}
