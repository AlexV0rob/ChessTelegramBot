package org.example;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.lang.Math;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Класс, который подключается к телеграму 
 */
public class TelegramBot implements LongPollingSingleThreadUpdateConsumer {
	/**
	 * Экземпляр класса TelegramClient 

	 */
	private final TelegramClient telegramClient;
	
	/**
	 * Ассоциативный массив с парами (ID чата телеграм, карточка пользователя)
	 */
	private HashMap<Long, User> users = new HashMap<>();
	
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
            
            //Получаем "карточку" пользователя, а если её нет, то создаём
            if (!users.containsKey(chatId)) {
            	users.put(chatId, new User());
            }
            User curUser = users.get(chatId);
            
            //Получем тексты ответов и создаём из них ответные сообщения
            ArrayList<String> responses = new MainLogic().processInput(incomingMessage, curUser);
            ArrayList<SendMessage> messages = createMessages(chatId, responses, curUser);
            
            //Отправление каждого по отдельности
            for (SendMessage message : messages) {
            	try {
                	telegramClient.execute(message);
            	} catch (TelegramApiException e) {
                	e.printStackTrace();
            	}
            }
        //Проверяем, есть ли callback запрос
        } else if (update.hasCallbackQuery()) {
            String callbackText = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            
            //Обработка текста callback запроса
            User curUser = users.get(chatId);
            ArrayList<MoveButton> newButtons = new MoveMaker()
            		.assembleMove(callbackText, curUser);
            
            //Новый текст сообщения - нулевая кнопка, после извлечения её убираем
            String newText = newButtons.get(0).getButtonMessage();
            newButtons.remove(0);
            
            //Изменяем сообщение с текстом хода
            EditMessageText newMessage = EditMessageText.builder()
                .chatId(chatId)
                .messageId(Math.toIntExact(messageId))
                .text(newText)
                .replyMarkup(InlineKeyboardMarkup
                        .builder()
                        .keyboard(createInlineKeyboard(newButtons))
                        .build())
                .build();
            try {
                telegramClient.execute(newMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Создать сообщения из текстов ответов
     * @param chatId
     * @param texts
     * @param curUser
     * @return экземпляр ArrayList, содержит сообщения (класс SendMessage)
     */
    private ArrayList<SendMessage> createMessages(long chatId,
    		ArrayList<String> texts, User curUser) {
    	switch (curUser.getMode()) {
    	/*
    	 * Коды режимов:
    	 * 0 - главное меню
    	 * 1 - режим эхо
    	 * 2 - одиночная игра (игра на одном устройстве)
    	 */
    	case 0:
    		return buildMenuMessages(chatId, texts);
    	case 1:
    		return buildEchoMessages(chatId, texts);
    	case 2:
    		return buildGameMessages(chatId, texts, curUser);
    	default:
    		return buildRegularMessages(chatId, texts);
    	}
    }
    
    /**
     * Построить сообщения в меню (Reply клавиатура)
     * @param chatId
     * @param texts
     * @return экземпляр ArrayList, содержит сообщения (класс SendMessage)
     */
    private ArrayList<SendMessage> buildMenuMessages(long chatId, ArrayList<String> texts) {
    	ArrayList<SendMessage> messages = new ArrayList<SendMessage>();
    	for (String message : texts) {
    		messages.add(SendMessage
        		.builder()
               	.chatId(chatId)
               	.text(message)
               	.replyMarkup(ReplyKeyboardMarkup
                           .builder()
                           .keyboardRow(new KeyboardRow(
                          		 "Включить эхо-мод",
                          		 "Начать новую игру"))
                           .keyboardRow(new KeyboardRow("Открыть окно помощи"))
                           .resizeKeyboard(true)
                           .selective(true)
                           .build())
               	.build());
    	}
    	return messages;
    }
    
    /**
     * Построить сообщения в режиме эхо (Reply клавиатура)
     * @param chatId
     * @param texts
     * @return экземпляр ArrayList, содержит сообщения (класс SendMessage)
     */
    private ArrayList<SendMessage> buildEchoMessages(long chatId, ArrayList<String> texts) {
    	ArrayList<SendMessage> messages = new ArrayList<SendMessage>();
    	for (String message : texts) {
    		messages.add(SendMessage
        		.builder()
                .chatId(chatId)
                .text(message)
                .replyMarkup(ReplyKeyboardMarkup
                           .builder()
                           .keyboardRow(new KeyboardRow(
                          		 "/menu"))
                           .resizeKeyboard(true)
                           .selective(true)
                           .isPersistent(true)
                           .build())
                .build());
    	}
    	return messages;
    }
    
    /**
     * Построить сообщения в режиме игры (Inline клавиатура)
     * @param chatId
     * @param texts
     * @return экземпляр ArrayList, содержит сообщения (класс SendMessage)
     */
    private ArrayList<SendMessage> buildGameMessages(long chatId,
    		ArrayList<String> texts, User curUser) {
    	String lastButtonText = texts.removeLast();
    	//Все сообщения, кроме последнего должны быть обычными
    	ArrayList<SendMessage> messages = buildRegularMessages(chatId, texts);
    	
    	ArrayList<MoveButton> figures = new MoveMaker().whichFiguresLeft(curUser);
    	messages.add(SendMessage
    			.builder()
    			.chatId(chatId)
    			.text(lastButtonText)
    			.replyMarkup(new ReplyKeyboardRemove(true))
    			.replyMarkup(InlineKeyboardMarkup
                        .builder()
                        .keyboard(createInlineKeyboard(figures))
                        .build())
    			.build());
    	return messages;
    }
    
    /**
     * Построить обычные сообщения (без клавиатур)
     * @param chatId
     * @param texts
     * @return экземпляр ArrayList, содержит сообщения (класс SendMessage)
     */
    private ArrayList<SendMessage> buildRegularMessages(long chatId, ArrayList<String> texts) {
    	ArrayList<SendMessage> messages = new ArrayList<SendMessage>();
    	for (String message : texts) {
    		messages.add(SendMessage
       			.builder()
                .chatId(chatId)
                .text(message)
                .replyMarkup(new ReplyKeyboardRemove(true))
                .build());
    	}
    	return messages;
    }
    
    /**
     * Создать Inline клавиатуру
     * @param buttons
     * @return экземпляр ArrayList, содержит ряды клавиатуры (класс InlineKeyboardRow)
     */
    private ArrayList<InlineKeyboardRow> createInlineKeyboard(ArrayList<MoveButton> buttons) {
    	/*
    	 * В зависимости от числа кнопок выбирается наилучшее число (2, 3, 4 или 5)
    	 * кнопок в одном ряду по наименьшему остатку от деления на это число. Если
    	 * остатки совпадают, приоритет количества кнопок 2 -> 3 -> 5 -> 4.
    	 */
    	int buttonsCount = buttons.size();
    	int remOf2 = buttonsCount % 2;
    	int remOf3 = buttonsCount % 3;
    	int remOf4 = buttonsCount % 4;
    	int remOf5 = buttonsCount % 5;
    	int divisor = 1;

    	if (remOf2 <= remOf3 && remOf2 <= remOf5 && remOf2 <= remOf4) {
    		divisor = 2;
    	}
    	if (remOf3 <= remOf4 && remOf3 <= remOf5 && remOf3 <= remOf2) {
    		divisor = 3;
    	}
    	if (remOf5 <= remOf3 && remOf5 <= remOf4 && remOf5 <= remOf2) {
    		divisor = 5;
    	}
    	if (remOf4 <= remOf3 && remOf4 <= remOf5 && remOf4 <= remOf2) {
    		divisor = 4;
    	}
    	
    	//Создание самих кнопок
    	int rowsCount = buttons.size() / divisor;
        ArrayList<InlineKeyboardRow> buttonsRows = new ArrayList<InlineKeyboardRow>();
        for (int i = 0, j = 0, jBias = 0; i < rowsCount + 1; ++i) {
        	InlineKeyboardRow newRow = new InlineKeyboardRow();
        	for (jBias = 0; jBias < divisor && j < buttonsCount; ++jBias, ++j) {
        		newRow.add(InlineKeyboardButton
        				.builder()
        				.text(buttons.get(j).getButtonMessage())
        				.callbackData(buttons.get(j).getCallbackQuery())
        				.build());
        	}
        	buttonsRows.add(newRow);
        }
        return buttonsRows;
    }
}