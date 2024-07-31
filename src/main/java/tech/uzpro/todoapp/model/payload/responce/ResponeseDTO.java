package tech.uzpro.todoapp.model.payload.responce;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ResponeseDTO {
    private HttpStatus status;
    private int statusCode;
    private String message;
}
