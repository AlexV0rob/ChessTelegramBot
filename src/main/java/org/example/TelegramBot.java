package org.example;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.lang.Math;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.example.buttons.*;

/**
 * Класс, который работает с Телеграмом 
 */
public class TelegramBot implements LongPollingSingleThreadUpdateConsumer {
	/**
	 * Выполняет коммуникацию с Телеграмом
	 */
	private final TelegramClient telegramClient;	
	/**
	 * Отвечающего за обработку ввода
	 */
	private final MainLogic mainLogic = new MainLogic();	
	/**
	 * Преобразует пользовательский ввод в единый вид для дальнейшей обработки
	 */
	private final UserInputConverter userInputConverter = new UserInputConverter();	
	/**
	 * Создаёт клавиатуры для различных состояний бота
	 */
	private final KeyboardCreator keyboardCreator = new KeyboardCreator();
	
	/**
	 * Ассоциативный массив с парами (ID чата телеграм, состояние игры)
	 */
	private HashMap<Long, GameState> games = new HashMap<Long, GameState>();
	
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
            processTextMessage(
            		update.getMessage().getText(),
            		update.getMessage().getChatId());
        //Проверка на callback запрос
        } else if (update.hasCallbackQuery()) {
            processCallbackQuery(
            		update.getCallbackQuery().getData(),
            		Math.toIntExact(
            				update.getCallbackQuery().getMessage().getMessageId()),
            		update.getCallbackQuery().getMessage().getChatId());
        }
    }
    
    /**
     * Создать Reply клавиатуру
     */
    private List<KeyboardRow> createReplyKeyboard(List<SimpleButton> buttons) {
        List<KeyboardRow> buttonsRows = new ArrayList<KeyboardRow>();
        KeyboardRow currentRow = new KeyboardRow();
        int rowCounter = 0, buttonsInRow = 4;
        for (SimpleButton currentButton : buttons) {
        	if (rowCounter == buttonsInRow) {
        		buttonsRows.add(currentRow);
        		currentRow = new KeyboardRow();
        		rowCounter = 0;
        	}
        	currentRow.add(KeyboardButton
    				.builder()
    				.text(currentButton.text())
    				.build());
        	++rowCounter;
        }
        buttonsRows.add(currentRow);
        return buttonsRows;
    }    
    /**
     * Создать Inline клавиатуру
     */
    private List<InlineKeyboardRow> createInlineKeyboard(List<IdentificatedButton> buttons) {
        List<InlineKeyboardRow> buttonsRows = new ArrayList<InlineKeyboardRow>();
        InlineKeyboardRow currentRow = new InlineKeyboardRow();
        int rowCounter = 0, buttonsInRow = 4;
        for (IdentificatedButton currentButton : buttons) {
        	if (rowCounter == buttonsInRow) {
        		buttonsRows.add(currentRow);
        		currentRow = new InlineKeyboardRow();
        		rowCounter = 0;
        	}
        	currentRow.add(InlineKeyboardButton
    				.builder()
    				.text(currentButton.text())
    				.callbackData(currentButton.id())
    				.build());
        	++rowCounter;
        }
        buttonsRows.add(currentRow);
        return buttonsRows;
    }
    
    /**
     * Послать сообщение
     * @return ID только что отправленного сообщения, -1 в случае ошибки
     */
    private int sendMessage(long chatId, String messageText,
    		ReplyKeyboard keyboardMarkup) {
    	SendMessage outgoingMessage = SendMessage
        		.builder()
               	.chatId(chatId)
               	.text(messageText)
               	.replyMarkup(keyboardMarkup)
               	.build();
        try {
           	Message sentMessage = telegramClient.execute(outgoingMessage);
           	return sentMessage.getMessageId();
        } catch (TelegramApiException e) {
        	System.out.println("Couldn't send message to Telegram: " + e);
           	e.printStackTrace();
           	return -1;
        }
    }
    /**
     * Изменить сообщение
     */
    private void editMessage(long chatId, int messageId, String messageText,
    		InlineKeyboardMarkup keyboardMarkup) {
    	EditMessageText updatedMessage = EditMessageText.builder()
        		.chatId(chatId)
                .messageId(messageId)
                .text(messageText)
                .replyMarkup(keyboardMarkup)
                .build();
        try {
        	telegramClient.execute(updatedMessage);
        } catch (TelegramApiException e) {
        	System.out.println("Couldn't edit message in Telegram: " + e);
        	e.printStackTrace();
        }
    }
    
    /**
     * Обработать текстовое сообщение, полученное от пользователя
     */
    private void processTextMessage(String incomingMessage, long chatId) {
    	if (!games.containsKey(chatId)) {
        	games.put(chatId, new GameState());
        }
        GameState currentGameState = games.get(chatId);
        
        List<String> responseMessagesTexts = mainLogic.processUserInput(
        		userInputConverter.convertFromTelegramMessage(
        				incomingMessage,
        				currentGameState.isInGame()),
        		currentGameState);
        //Для каждого сообщения нужно выставить параметр клавиатуры
        List<ReplyKeyboard> keyboardsForMessages = new ArrayList<ReplyKeyboard>();
        /*
         * Все сообщения, кроме последнего, должны быть без клавиатуры, 
         * а клавиатура последнего зависит от текущего режима
         */
        for (int i = 0; i < responseMessagesTexts.size() - 1; ++i) {
        	keyboardsForMessages.add(new ReplyKeyboardRemove(true));
        }
        if (currentGameState.isNoGame()) {
        	keyboardsForMessages.add(ReplyKeyboardMarkup
        		.builder()
                .keyboard(createReplyKeyboard(keyboardCreator.menuButtons()))
                .resizeKeyboard(true)
                .selective(true)
                .build());
        } else if (currentGameState.isInGame()) {
        	keyboardsForMessages.add(InlineKeyboardMarkup
            	.builder()
                .keyboard(createInlineKeyboard(
                		keyboardCreator.gameButtons(currentGameState)))
                .build());
        } else {
        	keyboardsForMessages.add(new ReplyKeyboardRemove(true));
        }
        
        Iterator<String> responseMessages = responseMessagesTexts.iterator();
        Iterator<ReplyKeyboard> messagesKeyboards = keyboardsForMessages.iterator();
        
        while (responseMessages.hasNext() && messagesKeyboards.hasNext()) {
        	sendMessage(chatId, responseMessages.next(), messagesKeyboards.next());
        }
    }
    /**
     * Обработать callback запрос, полученный от пользователя
     */
    private void processCallbackQuery(String callbackData, int messageId, long chatId) {
    	if (!games.containsKey(chatId)) {
        	games.put(chatId, new GameState());
        }
        GameState currentGameState = games.get(chatId);
        
        String editedMessageText = mainLogic.processUserInput(
        		userInputConverter.convertFromTelegramCallback(
        				callbackData,
        				currentGameState.isInGame()),
        		currentGameState).getFirst();
        
        //Если не в игре, Inline клавиатура не нужна
        InlineKeyboardMarkup keyboard;
        if (currentGameState.isInGame()) {
        	keyboard = InlineKeyboardMarkup
    				.builder()
    				.keyboard(createInlineKeyboard(
    						keyboardCreator.gameButtons(currentGameState)))
    				.build();
        } else {
        	keyboard = InlineKeyboardMarkup.builder().build();
        }
        
        editMessage(chatId, messageId, editedMessageText, keyboard);
        
        //Если ход полностью собран, его нужно реализовать
        if (currentGameState.isMoveReady()) {
        	processTextMessage(currentGameState.assembleMove(), chatId);
		}
    }
}