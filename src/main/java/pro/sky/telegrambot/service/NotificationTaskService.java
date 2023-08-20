package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.exception.InvalidNotificationFormatException;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
@Slf4j
@AllArgsConstructor
@Service
public class NotificationTaskService {

    private final TelegramBot telegramBot;
    private final NotificationTaskRepository repository;
    private final ParseService parse;

    public void saveNotification(Message message) {
        try {
            repository.save(parse.notification(message));
        } catch (InvalidNotificationFormatException e) {
            log.warn("id чата: {}, не поддерживаемый формат напоминания: {}", message.chat().id(), message.text());
            telegramBot.execute(new SendMessage(message.chat().id(), e.getMessage()));
        }
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void sendNotifications() {
        var notifications = repository.findNotificationTasksByNotificationDate(
                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)
        );
        if (!notifications.isEmpty()) {
            for (NotificationTask notification : notifications) {
                telegramBot.execute(
                        new SendMessage(notification.getChatId(), notification.getNotification())
                );
            }
        }
    }

}
