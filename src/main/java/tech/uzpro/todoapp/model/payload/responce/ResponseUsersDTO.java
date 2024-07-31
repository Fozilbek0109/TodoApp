package tech.uzpro.todoapp.model.payload.responce;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import tech.uzpro.todoapp.domain.User;

import java.util.List;
@Data
@Builder
public class ResponseUsersDTO {
    private HttpStatus status;
    private int statucCode;
    private String message;
    private List<User> users;
}
