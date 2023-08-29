package pro.sky.telegrambot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableAutoConfiguration(exclude = LiquibaseAutoConfiguration.class)
class TelegramBotApplicationTests {

	@Test
	void contextLoads() {
	}

}
