package org.example;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
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
	
	/**
	 * Конструктор класса
	 * Получение токена бота из Main
	 */
	public TelegramBot(String botToken) {
        telegramClient = new OkHttpTelegramClient(botToken);
    }
    @Override
    public void consume(Update update) {
    	//Смотрим, получили ли сообщение и есть ли в нём текст
        if (update.hasMessage() && update.getMessage().hasText()) {
            String incomingMessage = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            String response = mainLogic.processInput(incomingMessage);
            
            //Создаём исходящее сообщение
            SendMessage outgoingMessage = SendMessage
            		.builder()
                    .chatId(chatId)
                    .text(response)
                    .build();
            
            //Пытаемся отослать исходящее сообщение
            try {
                telegramClient.execute(outgoingMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}