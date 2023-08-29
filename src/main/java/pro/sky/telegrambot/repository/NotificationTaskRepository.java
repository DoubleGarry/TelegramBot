package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pro.sky.telegrambot.model.NotificationTask;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Long> {
    @Query(value = "SELECT * FROM tasks WHERE task_date_time =:date_time", nativeQuery = true)
    List<NotificationTask> findTasks(LocalDateTime date_time);
}
