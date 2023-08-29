package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name="tasks")
public class NotificationTask {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    @Column(name = "chat_id")
    private Long chat_id;
    @Column(name = "text", columnDefinition = "text")
    private String text;
    @Column(name = "task_date_time")
    private LocalDateTime task_date_time;

    public NotificationTask(Long chat_id, String text, LocalDateTime task_date_time) {
        this.chat_id = chat_id;
        this.text = text;
        this.task_date_time = task_date_time;
    }

    public NotificationTask() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getChat_id() {
        return chat_id;
    }

    public void setChat_id(Long chat_id) {
        this.chat_id = chat_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getTask_date_time() {
        return task_date_time;
    }

    public void setTask_date_time(LocalDateTime task_date_time) {
        this.task_date_time = task_date_time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NotificationTask)) return false;

        NotificationTask that = (NotificationTask) o;

        if (id != that.id) return false;
        if (!Objects.equals(chat_id, that.chat_id)) return false;
        if (!Objects.equals(text, that.text)) return false;
        return Objects.equals(task_date_time, that.task_date_time);
    }

    @Override
    public int hashCode() {
        return id;
    }
}
