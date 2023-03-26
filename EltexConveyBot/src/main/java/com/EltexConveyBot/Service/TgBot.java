package com.EltexConveyBot.Service;

import com.EltexConveyBot.Config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.ExecutionException;
@Slf4j
@Component
public class TgBot extends TelegramLongPollingBot {

    final BotConfig config;

    public TgBot(BotConfig config) {
        this.config = config;
    }

    @Override
    public void onUpdateReceived(Update update) {


        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();


            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                default:
                    sendMessage(chatId, "Команда не поддерживается.");
            }
        }

    }

    private void startCommandReceived(long chatId, String name) {
        String answer = "Привет, " + name + ", это бот-помощник на конвейере!\nПока мой функционал сильно ограничен. Разработчик будет постепенно добавлять новые фичи для твоего удобства.";
        log.info("Ответ пользователю " + name);
        sendMessage(chatId, answer);

    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Ошибка: "+ e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    public String getBotToken() {
        return config.getBotToken();
    }

}
