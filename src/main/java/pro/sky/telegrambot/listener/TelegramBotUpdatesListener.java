package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.service.NotificationTaskService;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final NotificationTaskService service;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, NotificationTaskService service) {
        this.telegramBot = telegramBot;
        this.service = service;
    }


    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {

        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            Long chatId = update.message().chat().id();
            String message = update.message().text();

            if (update.message().text().equals("/start")){
                if (service.replyStart(chatId) == null) {
                    service.sendSomethingWrong(chatId);
                }
            } else {
                if (service.saveTask(chatId, message) == null) {
                    service.sendSomethingWrong(chatId);
                }
            }

        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
