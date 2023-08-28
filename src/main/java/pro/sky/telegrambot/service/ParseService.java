package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.Message;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.exception.InvalidNotificationFormatException;
import pro.sky.telegrambot.model.NotificationTask;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.regex.Pattern;

@Service
public class ParseService {

    public NotificationTask notification(Message message) throws InvalidNotificationFormatException {
        var pattern = Pattern.compile("([0-9.:\\s]{16})(\\s)([\\W+]+)");
        var matcher = pattern.matcher(message.text());
        if (matcher.matches()) {
            String date = matcher.group(1);
            String notification = matcher.group(3);
            var parseDate = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
            return NotificationTask.builder()
                    .chatId(message.chat().id())
                    .notification(notification)
                    .notificationDate(parseDate)
                    .build();
        }
        throw new InvalidNotificationFormatException();
    }

    public String command(Message message) {
        var commandEntity = Arrays.stream(message.entities())
                .filter(e -> "bot_command".equals(e.type().name()))
                .findFirst();
        return message.text().substring(
                commandEntity.get().offset(), commandEntity.get().length()
        );
    }

}
