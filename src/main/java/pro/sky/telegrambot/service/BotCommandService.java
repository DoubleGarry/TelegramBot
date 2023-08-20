package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@AllArgsConstructor
public class BotCommandService {

    private final TelegramBot telegramBot;
    private final ParseService parse;

    public void beginCommand(Message message) {
        var command = parse.command(message);

        switch (command) {
            case "/start":
                sendGreetings(message);
                break;
            case "/info":
                sendInfo(message);
                break;
            default:
                log.warn("id чата: {}, не поддерживаемая команда: {}", message.chat().id(), message.text());
                unsupportedCommand(message);
        }
    }

    private void sendGreetings(Message message) {
        var sendMessage = new SendMessage(
                message.chat().id(),
                "Привет, " + message.chat().firstName() + ", рад тебя видеть, " +
                        "для получения информации как пользоваться ботом набери /info!"
        );
        telegramBot.execute(sendMessage);
    }

    private void sendInfo(Message message) {
        var sendMessage = new SendMessage(
                message.chat().id(),
                "Я пришлю тебе уведомление, что бы ты не забыл что-то важное!\n " +
                        "Для того, что бы создать напоминание напечатай сообщение по шаблону:\n" +
                        "01.01.2022 20:00 Сделать домашнюю работу"
        );
        telegramBot.execute(sendMessage);
    }

    private void unsupportedCommand(Message message) {
        telegramBot.execute(new SendMessage(message.chat().id(), "Не поддерживаемая команда"));
    }

}
