package pro.sky.telegrambot.exception;

public class InvalidNotificationFormatException extends Exception {

    @Override
    public String getMessage() {
        return "Не верный запрос, воспользуйся /info";
    }

}
