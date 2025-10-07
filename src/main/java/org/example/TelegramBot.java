package org.example;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import static java.lang.Math.toIntExact;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Класс, который подключается к телеграму и обрабатывает входящие команды
 */
public class TelegramBot implements LongPollingSingleThreadUpdateConsumer {
	/**
	 * Экземпляр класса TelegramClient 

	 */
	private final TelegramClient telegramClient;
	
	/**
	 * Экземпляр класса MainLogic
	 */
	private MainLogic mainLogic = new MainLogic();

	private HashMap<Long, User> users = new HashMap<>();
	
	/**
	 * Конструктор класса
	 * Получение токена бота из Main
	 */
	public TelegramBot(String botToken) {
        telegramClient = new OkHttpTelegramClient(botToken);
    }
	
	/**
	 * Получает на вход update и передаёт управление MainLogic
	 */
    @Override
    public void consume(Update update) {
    	//Смотрим, получили ли сообщение и есть ли в нём текст
        if (update.hasMessage() && update.getMessage().hasText()) {
            String incomingMessage = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            if (!users.containsKey(chatId)) {
            	users.put(chatId, new User(chatId));
            }
            User curUser = users.get(chatId);
            
            //Список ответных сообщений
            ArrayList<String> responses = mainLogic.processInput(incomingMessage, curUser);
            //Проходимся по списку и отправляем каждое
            for (String message : responses) {
            	//Создаём исходящее сообщение
            	SendMessage outgoingMessage = SendMessage
            			.builder()
                    	.chatId(chatId)
                    	.text(message)
                    	.build();
            
            	//Пытаемся отослать исходящее сообщение
            	try {
                	telegramClient.execute(outgoingMessage);
            	} catch (TelegramApiException e) {
                	e.printStackTrace();
            	}
            }
        //Если нет, приверяем, есть ли callback
        }
    }
}