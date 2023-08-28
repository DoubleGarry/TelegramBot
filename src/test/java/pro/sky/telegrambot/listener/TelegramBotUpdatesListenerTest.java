package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.service.NotificationTaskService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TelegramBotUpdatesListenerTest {

    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;
    @Mock
    private NotificationTaskService service;

    @InjectMocks
    private TelegramBotUpdatesListener out;

    @BeforeEach
    public void init(){
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
    }

    @Test
    void processTestStartMessage(){
        when(chat.id()).thenReturn(1L);
        when(message.text()).thenReturn("/start");
        assertEquals(out.process(List.of(update)), -1);
        verify(service, atLeastOnce()).replyStart(1L);
    }

    @Test
    void processTestStartWrongChatId(){
        when(chat.id()).thenReturn(-1L);
        when(message.text()).thenReturn("/start");
        when(service.replyStart(-1L)).thenReturn(null);
        assertEquals(out.process(List.of(update)), -1);
        verify(service, atLeastOnce()).sendSomethingWrong(-1L);
    }

    @Test
    void processTestTaskMessage() {
        when(chat.id()).thenReturn(1L);
        when(message.text()).thenReturn("01.01.2022 20:00 Сделать домашнюю работу");
        assertEquals(out.process(List.of(update)), -1);
        verify(service, atLeastOnce()).saveTask(any(),any());
    }

    @Test
    void processTestTaskMessageWrongChatID() {
        when(chat.id()).thenReturn(-1L);
        when(message.text()).thenReturn("01.01.2022 20:00 Сделать домашнюю работу");
        when(service.saveTask(-1L,"01.01.2022 20:00 Сделать домашнюю работу")).thenReturn(null);
        assertEquals(out.process(List.of(update)), -1);
        verify(service, atLeastOnce()).sendSomethingWrong(-1L);
    }

    @Test
    void processTestTaskMessageWrongText() {
        when(chat.id()).thenReturn(1L);
        when(message.text()).thenReturn("Сделать работу");
        when(service.saveTask(1L,"Сделать работу")).thenReturn(null);
        assertEquals(out.process(List.of(update)), -1);
        verify(service, atLeastOnce()).sendSomethingWrong(1L);
    }

}
