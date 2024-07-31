package tech.uzpro.todoapp.model.payload.tasks;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreatedTaskDTO {
    @NotBlank(message = "User Id is required")
    private Long userId;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotBlank(message = "Priority is required")
    private String priority;

    private Long dueDate;

    private boolean completed;
}
