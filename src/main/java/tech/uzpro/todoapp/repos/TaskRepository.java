package tech.uzpro.todoapp.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.uzpro.todoapp.domain.Task;
import tech.uzpro.todoapp.model.enums.Priority;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findAllByUserId(Long user_id);

//    List<Task> findAllByUserIdAndCompleted(Long user_id, boolean completed);

    List<Task> findAllByUserIdAndPriority(Long user_id, Priority priority);

    List<Task> findAllByPriority(Priority priority);

    List<Task> findAllByDueDate(Long dueDate);

    List<Task> findAllByIsCompleted(boolean completed);
}
