package org.example;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.lang.Math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

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
	private HashMap<Long, GameState> games = new HashMap<Long, GameState>();
	
	private MainLogic mainLogic = new MainLogic();
	
	private UserInputConverter userInputConverter = new UserInputConverter();
	
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
            
            if (!games.containsKey(chatId)) {
            	games.put(chatId, new GameState());
            }
            GameState currentGameState = games.get(chatId);
            
            String responseMessageText = mainLogic.processUserInput(
            		userInputConverter.convertFromMessage(
            				incomingMessage,
            				currentGameState.currentState().equals(
            						GameState.STATES.INGAME)),
            		currentGameState);
            
            ReplyKeyboard keyboard;
            switch (currentGameState.currentState()) {
            case GameState.STATES.NOGAME:
            	keyboard = ReplyKeyboardMarkup
            		.builder()
                    .keyboardRow(new KeyboardRow(
                    		"Начать игру на этом устройстве"))
                    .resizeKeyboard(true)
                    .selective(true)
                    .build();
            	break;
            case GameState.STATES.INGAME:
            	keyboard = InlineKeyboardMarkup
                	.builder()
                    .keyboard(createInlineKeyboard(
                    		currentGameState.nextButtonsLine()))
                    .build();
            	break;
            default:
            	keyboard = new ReplyKeyboardRemove(true);
            }
            SendMessage outgoingMessage = SendMessage
            		.builder()
                   	.chatId(chatId)
                   	.text(responseMessageText)
                   	.replyMarkup(keyboard)
                   	.build();
            try {
               	telegramClient.execute(outgoingMessage);
            } catch (TelegramApiException e) {
               	e.printStackTrace();
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            
            if (!games.containsKey(chatId)) {
            	games.put(chatId, new GameState());
            }
            GameState currentGameState = games.get(chatId);
            
            String editedMessageText = mainLogic.processUserInput(
            		userInputConverter.convertFromCallback(
            				callbackData,
            				currentGameState.currentState().equals(
            						GameState.STATES.INGAME)),
            		currentGameState);
            
            EditMessageText updatedMessage = EditMessageText.builder()
            		.chatId(chatId)
                    .messageId(Math.toIntExact(messageId))
                    .text(editedMessageText)
                    .replyMarkup(InlineKeyboardMarkup
                            .builder()
                            .keyboard(createInlineKeyboard(
                            		currentGameState.nextButtonsLine()))
                            .build())
                    .build();
            try {
                telegramClient.execute(updatedMessage);
            } catch (TelegramApiException e) {
            	e.printStackTrace();
            }
        }
    }
    /**
     * Создать Inline клавиатуру
     */
    private List<InlineKeyboardRow> createInlineKeyboard(List<ButtonWithID> buttons) {
        List<InlineKeyboardRow> buttonsRows = new LinkedList<InlineKeyboardRow>();
        InlineKeyboardRow currentRow = new InlineKeyboardRow();
        int rowCounter = 0, buttonsInRow = 4;
        for (ButtonWithID currentButton : buttons) {
        	if (rowCounter == buttonsInRow) {
        		buttonsRows.add(currentRow);
        		currentRow = new InlineKeyboardRow();
        		rowCounter = 0;
        	}
        	currentRow.add(InlineKeyboardButton
    				.builder()
    				.text(currentButton.buttonText)
    				.callbackData(currentButton.buttonID)
    				.build());
        	++rowCounter;
        }
        buttonsRows.add(currentRow);
        return buttonsRows;
    }
}