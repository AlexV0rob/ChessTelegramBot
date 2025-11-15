package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.example.buttons.IdentifiedButton;
import org.example.buttons.SimpleButton;

import org.example.inputHandlers.CommandHandler;
import org.example.inputHandlers.InGameInputHandler;
import org.example.inputHandlers.MainMenuInputHandler;

import org.example.states.MoveState;
import org.example.states.UserState;

import java.util.List;

/**
 * Проверка главного логического модуля 
 */
public class MainLogicTest {
	/**
	 * Главный логический модуль
	 */
	private final MainLogic mainLogic = new MainLogic();
	
	/**
	 * Обработчик комманд
	 */
	private final CommandHandler commandHandler = new CommandHandler();
	
	/**
	 * Обработчик ввода в игре
	 */
	private final InGameInputHandler inGameInputHandler = 
			new InGameInputHandler();
	
	/**
	 * Обработчик ввода в главном меню
	 */
	private final MainMenuInputHandler mainMenuInputHandler = 
			new MainMenuInputHandler();
	
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
		UserState userStateExpected = new UserState();
		userStateExpected.setUserState(UserState.USER_STATE.MAINMENU);
		List<String> responseReal = mainLogic.processInput("/newsinglegame", 0);
		List<String> responseExpected = 
				commandHandler.processCommand("newsinglegame", "", userStateExpected);
		Assertions.assertIterableEquals(responseExpected, responseReal);
	}
	
	/**
	 * Проверить ввод в игре
	 */
	@Test
	public void userInputInGameTest() {
		UserState userStateExpected = new UserState();
		userStateExpected.setUserState(UserState.USER_STATE.INGAME);
		mainLogic.processInput("/newsinglegame", 0);
		List<String> responseReal = mainLogic.processInput("something", 0);
		List<String> responseExpected = 
				inGameInputHandler.processInput("something", userStateExpected);
		Assertions.assertIterableEquals(responseExpected, responseReal);
	}
	
	/**
	 * Проверить ввод в главном меню
	 */
	@Test
	public void userInputInMenuTest() {
		UserState userStateExpected = new UserState();
		userStateExpected.setUserState(UserState.USER_STATE.MAINMENU);
		mainLogic.processInput("/quit", 0);
		List<String> responseReal = mainLogic.processInput("something", 0);
		List<String> responseExpected = 
				mainMenuInputHandler.processInput("something", userStateExpected);
		Assertions.assertIterableEquals(responseExpected, responseReal);
	}
	
	/**
	 * Проверить кнопки в игре
	 */
	@Test
	public void buttonsInGameTest() {
		MoveState moveStateExpected = new MoveState();
		mainLogic.processInput("/newsinglegame", 0);
		List<SimpleButton> simpleButtonsReal = mainLogic.getCurrentSimpleButtons(0);
		List<IdentifiedButton> identifiedButtonsReal = 
				mainLogic.getCurrentIdentifiedButtons(0);
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
		mainLogic.processInput("/quit", 0);
		List<SimpleButton> simpleButtonsReal = mainLogic.getCurrentSimpleButtons(0);
		List<IdentifiedButton> identifiedButtonsReal = 
				mainLogic.getCurrentIdentifiedButtons(0);
		List<SimpleButton> simpleButtonsExpected = 
				buttonsCreator.getMenuButtons();
		List<IdentifiedButton> identifiedButtonsExpected = List.of();
				
		Assertions.assertIterableEquals(simpleButtonsExpected, simpleButtonsReal);
		Assertions.assertIterableEquals(identifiedButtonsExpected, identifiedButtonsReal);
	}
}
