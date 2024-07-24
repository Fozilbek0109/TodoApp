package tech.uzpro.todoapp.model.payload.tasks;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GetTaskDTO {

    @NotBlank(message = "Id is required")
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotBlank(message = "Priority is required")
    private String priority;

    @NotBlank(message = "Due date is required")
    private Long dueDate;

    private boolean completed;


}
