package org.example.bots;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.example.MainLogic;
import org.example.auxiliary.IdentifiedButton;
import org.example.auxiliary.SimpleButton;
import org.example.states.UserState;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, который подключается к телеграму 
 */
public class TelegramBot implements LongPollingSingleThreadUpdateConsumer, Bot {
	/**
	 * Экземпляр класса TelegramClient
	 */
	private final TelegramClient telegramClient;
	
	/**
	 * Экземпляр класса MainLogic, обрабатывает входящие сообщения
	 */
	private final MainLogic logic;
	
	/**
	 * Конструктор класса
	 * Получение токена бота из Main
	 */
	public TelegramBot(String botToken, MainLogic mainLogic) {
        telegramClient = new OkHttpTelegramClient(botToken);
        logic = mainLogic;
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
            		update.getCallbackQuery().getMessage().getChatId());
        }
    }
    
    /**
     * Создать Reply клавиатуру
     */
    private List<KeyboardRow> createReplyKeyboard(List<SimpleButton> buttons) {
        List<KeyboardRow> buttonsRows = new ArrayList<KeyboardRow>();
        KeyboardRow currentRow;
        for (SimpleButton currentButton : buttons) {
        	currentRow = new KeyboardRow();
        	currentRow.add(KeyboardButton
    				.builder()
    				.text(currentButton.buttonText())
    				.build());
        	buttonsRows.add(currentRow);
        }
        return buttonsRows;
    }
    
    /**
     * Создать Inline клавиатуру
     */
    private List<InlineKeyboardRow> createInlineKeyboard(List<IdentifiedButton> buttons) {
        List<InlineKeyboardRow> buttonsRows = new ArrayList<InlineKeyboardRow>();
        InlineKeyboardRow currentRow = new InlineKeyboardRow();
        int rowCounter = 0, buttonsInRow = 4;
        for (IdentifiedButton currentButton : buttons) {
        	if (rowCounter == buttonsInRow) {
        		buttonsRows.add(currentRow);
        		currentRow = new InlineKeyboardRow();
        		rowCounter = 0;
        	}
        	currentRow.add(InlineKeyboardButton
    				.builder()
    				.text(currentButton.buttonText())
    				.callbackData(currentButton.buttonId())
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
     * Обработать текстовое сообщение, полученное от пользователя
     */
    private void processTextMessage(String incomingMessage, long chatId) {
    	logic.processInput(this, incomingMessage, chatId);
    }
    
    /**
     * Обработать callback запрос, полученный от пользователя
     */
    private void processCallbackQuery(String callbackData, long chatId) {
        logic.processInput(this, callbackData, chatId);
    }
    
    @Override
    public long sendMessages(long chatId, List<String> messagesTexts) {
    	String lastMessage = messagesTexts.removeLast();
    	for (int i = 0; i < messagesTexts.size(); ++i) {
        	sendMessage(chatId, messagesTexts.get(i), new ReplyKeyboardRemove(true));
        }
    	List<SimpleButton> replyButtons = 
    			logic.getCurrentSimpleButtons(this, chatId);
        List<IdentifiedButton> inlineButtons = 
        		logic.getCurrentIdentifiedButtons(this, chatId);
        ReplyKeyboard lastMessageKeyboard;
        if (!replyButtons.isEmpty()) {
        	lastMessageKeyboard = ReplyKeyboardMarkup
        			.builder()
        			.keyboard(createReplyKeyboard(replyButtons))
        			.resizeKeyboard(true)
        			.selective(true)
        			.oneTimeKeyboard(true)
        			.build();
        } else if (!inlineButtons.isEmpty()) {
        	lastMessageKeyboard = InlineKeyboardMarkup
        			.builder()
        			.keyboard(createInlineKeyboard(inlineButtons))
        			.build();
        } else {
        	lastMessageKeyboard = new ReplyKeyboardRemove(true);
        }
        long messageId = sendMessage(chatId, lastMessage, lastMessageKeyboard);
        if (inlineButtons.isEmpty()) {
        	return -1;
        } else {
        	return messageId;
        }
    }
    
    @Override
    public void editMessage(long chatId, long messageId, 
    		String editedMessageText, boolean moreMessages) {
    	InlineKeyboardMarkup editedMessageKeyboard = null;
        if (!moreMessages) {
        	List<IdentifiedButton> inlineButtons = 
        			logic.getCurrentIdentifiedButtons(this, chatId);
        	editedMessageKeyboard = InlineKeyboardMarkup
        			.builder()
        			.keyboard(createInlineKeyboard(inlineButtons))
        			.build();
        } else {
        	editedMessageKeyboard = InlineKeyboardMarkup.builder().build();
        }
        EditMessageText updatedMessage = EditMessageText.builder()
        		.chatId(chatId)
                .messageId(Math.toIntExact(messageId))
                .text(editedMessageText)
                .replyMarkup(editedMessageKeyboard)
                .build();
        try {
        	telegramClient.execute(updatedMessage);
        } catch (TelegramApiException e) {
        	System.out.println("Couldn't edit message in Telegram: " + e);
        	e.printStackTrace();
        }
    }

	@Override
	public UserState.MessengerType getBotMessengerType() {
		return UserState.MessengerType.TELEGRAM;
	}
}