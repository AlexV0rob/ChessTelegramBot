package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.example.auxiliary.IdentifiedButton;
import org.example.auxiliary.SimpleButton;
import org.example.bots.FakeBot;
import org.example.inputHandlers.CommandInputHandler;
import org.example.inputHandlers.InGameInputHandler;
import org.example.states.GameState;
import org.example.states.MoveState;
import org.example.states.UserState;

import java.util.List;

/**
 * Проверка главного логического модуля 
 */
public class MainLogicTest {
	/**
	 * Фальшивый аккумулирующий бот
	 */
	private final FakeBot fakeBot = new FakeBot();
	
	/**
	 * Пользовательское состояние для проверки ответов
	 */
	private UserState userStateExpected;
	
	/**
	 * Главный логический модуль
	 */
	private final MainLogic mainLogic = new MainLogic();
	
	/**
	 * Обработчик комманд
	 */
	private final CommandInputHandler commandHandler = new CommandInputHandler();
	
	/**
	 * Обработчик ввода в игре
	 */
	private final InGameInputHandler inGameInputHandler = 
			new InGameInputHandler();
	
	/**
	 * Создатель кнопок
	 */
	private final ButtonsCreator buttonsCreator = new ButtonsCreator();
	
	/**
	 * Начальная доска
	 */
	private final static byte[][] START_BOARD = {
			{-2, -3, -4, -5, -6, -4, -3, -2},
			{-1, -1, -1, -1, -1, -1, -1, -1},
			{ 0,  0,  0,  0,  0,  0,  0,  0},
			{ 0,  0,  0,  0,  0,  0,  0,  0},
			{ 0,  0,  0,  0,  0,  0,  0,  0},
			{ 0,  0,  0,  0,  0,  0,  0,  0},
			{ 1,  1,  1,  1,  1,  1,  1,  1},
			{ 2,  3,  4,  5,  6,  4,  3,  2},
	};
	
	/**
	 * Проверить ввод команды
	 */
	@Test
	public void userInputCommandTest() {
		fakeBot.clearMessages();
		userStateExpected = new UserState(null);
		userStateExpected.setUserState(UserState.UserStatus.MAINMENU);
		mainLogic.processInput(fakeBot, "/help", 0);
		List<String> responseReal = fakeBot.getAccumulatedMessages(0);
		List<String> responseExpected = commandHandler
				.processCommand("help").messagesTextsLists().getFirst();
		Assertions.assertIterableEquals(responseExpected, responseReal);
	}
	
	/**
	 * Проверить ввод в игре
	 */
	@Test
	public void userInputInGameTest() {
		fakeBot.clearMessages();
		userStateExpected = new UserState(null);
		userStateExpected.setUserState(UserState.UserStatus.INGAME);
		GameState gameStateExpected = new GameState();
		mainLogic.processInput(fakeBot, "/newsinglegame", 0);
		fakeBot.clearMessages();
		mainLogic.processInput(fakeBot, "something", 0);
		List<String> responseReal = fakeBot.getAccumulatedMessages(0);
		List<String> responseExpected = inGameInputHandler
				.processInputSingleGame(
						"something", 
						userStateExpected.getMoveState(), 
						gameStateExpected)
				.messagesTextsLists().getFirst();
		Assertions.assertIterableEquals(responseExpected, responseReal);
	}
	
	/**
	 * Проверить ввод в главном меню
	 */
	@Test
	public void userInputInMenuTest() {
		fakeBot.clearMessages();
		userStateExpected = new UserState(null);
		userStateExpected.setUserState(UserState.UserStatus.MAINMENU);
		mainLogic.processInput(fakeBot, "/quit", 0);
		fakeBot.clearMessages();
		mainLogic.processInput(fakeBot, "something", 0);
		List<String> responseReal = fakeBot.getAccumulatedMessages(0);
		List<String> responseExpected = commandHandler
				.processCommand("something")
				.messagesTextsLists().getFirst();
		Assertions.assertIterableEquals(responseExpected, responseReal);
	}
	
	/**
	 * Проверить кнопки в игре
	 */
	@Test
	public void buttonsInGameTest() {
		MoveState moveStateExpected = new MoveState();
		mainLogic.processInput(fakeBot, "/newsinglegame", 0);
		List<SimpleButton> simpleButtonsReal = mainLogic.getCurrentSimpleButtons(fakeBot, 0);
		List<IdentifiedButton> identifiedButtonsReal = 
				mainLogic.getCurrentIdentifiedButtons(fakeBot, 0);
		List<SimpleButton> simpleButtonsExpected = List.of();
		List<IdentifiedButton> identifiedButtonsExpected = 
				buttonsCreator.getGameButtons(moveStateExpected, START_BOARD, true);
		Assertions.assertIterableEquals(simpleButtonsExpected, simpleButtonsReal);
		Assertions.assertIterableEquals(identifiedButtonsExpected, identifiedButtonsReal);
	}
	
	/**
	 * Проверить кнопки в главном меню
	 */
	@Test
	public void buttonsInMenuTest() {
		mainLogic.processInput(fakeBot, "/quit", 0);
		List<SimpleButton> simpleButtonsReal = mainLogic.getCurrentSimpleButtons(fakeBot, 0);
		List<IdentifiedButton> identifiedButtonsReal = 
				mainLogic.getCurrentIdentifiedButtons(fakeBot, 0);
		List<SimpleButton> simpleButtonsExpected = 
				buttonsCreator.getMenuButtons();
		List<IdentifiedButton> identifiedButtonsExpected = List.of();
				
		Assertions.assertIterableEquals(simpleButtonsExpected, simpleButtonsReal);
		Assertions.assertIterableEquals(identifiedButtonsExpected, identifiedButtonsReal);
	}
}
