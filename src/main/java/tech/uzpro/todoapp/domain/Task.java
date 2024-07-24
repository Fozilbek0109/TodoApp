package tech.uzpro.todoapp.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.uzpro.todoapp.model.enums.Priority;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(name = "due_date")
    private Long dueDate;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @ManyToOne
    private User user;

    @Column(name = "is_completed")
    private boolean isCompleted = false;


    @Column(name = "created_at", updatable = false)
    private Long createdAt;

    @Column(name = "updated_at")
    private Long updatedAt;
}
