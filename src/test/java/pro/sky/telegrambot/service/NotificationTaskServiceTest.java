package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.internal.matchers.Null;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeastOnce;


@SpringBootTest
@AutoConfigureTestDatabase
class NotificationTaskServiceTest {

    @Autowired
    private NotificationTaskRepository repository;
    @MockBean
    private Message message;
    @MockBean
    private SendResponse sendResponse;
    @MockBean
    private TelegramBot telegramBot;

    @InjectMocks
    @Autowired
    private NotificationTaskService out;


    @AfterEach
    public void clean(){
        repository.deleteAll();
    }

    @Test
    void replyStartTestNull(){
        assertNull(out.replyStart(null));
    }

    @Test
    void replyStartTestNegative(){
        assertNull(out.replyStart(-1L));
    }

    @Test
    void replyStartTestValid(){
        when(telegramBot.execute(any())).thenReturn(sendResponse);
        when(sendResponse.message()).thenReturn(message);
        when(message.text()).thenReturn("Привет, рад тебя видеть!");

        assertNotNull(out.replyStart(1L));
        String START_MESSAGE = "Привет, рад тебя видеть!";
        assertEquals(START_MESSAGE, out.replyStart(1L).message().text());
        verify(telegramBot, atLeastOnce()).execute(any());
    }

    public static Stream<Arguments> setTestParams() {
        return Stream.of(
                Arguments.of(1L,"Какое-то сообщение"),
                Arguments.of(1L,null),
                Arguments.of(null,"Какое-то сообщение"),
                Arguments.of(null,null),
                Arguments.of(-1L,"")
        );
    }
    @ParameterizedTest
    @MethodSource("setTestParams")
    void saveTaskTestWrongMessage(Long chatId, String message){
        assertNull(out.saveTask(chatId,message));
    }

    @Test
    void saveTaskValidMessage(){

        NotificationTask task = new NotificationTask(1L,"Сделать домашнюю работу",
                LocalDateTime.parse("01.01.2022 20:00", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));

        when(telegramBot.execute(any())).thenReturn(sendResponse);

        when(telegramBot.execute(any())).thenReturn(sendResponse);
        when(sendResponse.message()).thenReturn(message);
        when(message.text()).thenReturn("Новая задача успешно добавлена!");

        SendResponse responseOut = out.saveTask(1L,"01.01.2022 20:00 Сделать домашнюю работу");

        assertEquals("Новая задача успешно добавлена!", responseOut.message().text());

        assertNotNull(responseOut);

        List<NotificationTask> list = repository.findAll();
        assertFalse(list.isEmpty());
        assertEquals(1,list.size());
        assertEquals("Сделать домашнюю работу",list.get(0).getText());
    }

    @Test
    void findTasksTest() throws InterruptedException{
        NotificationTask task = new NotificationTask(1L,"Сделать домашнюю работу",
                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        repository.save(task);
        out.findTasks();
        List<NotificationTask> list1 = repository.findTasks(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        assertFalse(list1.isEmpty());
        verify(telegramBot,atLeastOnce()).execute(any());

    }

}
