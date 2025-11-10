package org.example.inputHandlers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.example.states.UserState;

import java.util.List;

/**
 * Проверка обрабтчика ввода в главном меню
 */
public class MainMenuInputHandlerTest {
	/**
	 * Обработчик ввода в главном меню
	 */
	private final MainMenuInputHandler mainMenuInputHandler = 
			new MainMenuInputHandler();
	
	/**
	 * Проверка запроса начала игры
	 */
	@Test
	public void startGameTest() {
		UserState userState = new UserState();
		userState.setUserState(UserState.USER_STATE.MAINMENU);
		mainMenuInputHandler.processInput(
				"Начать игру на этом устройстве", userState);
		Assertions.assertEquals(userState.getUserState(), UserState.USER_STATE.INGAME);
	}
	
	/**
	 * Проверка неизвестного запроса
	 */
	@Test
	public void unknownQueryTest() {
		UserState userState = new UserState();
		userState.setUserState(UserState.USER_STATE.MAINMENU);
		List<String> unknownQuery = mainMenuInputHandler.processInput(
				"Какой-то запрос", userState);
		Assertions.assertEquals(
				userState.getUserState(), UserState.USER_STATE.MAINMENU);
		Assertions.assertIterableEquals(
				List.of("Неизвестный запрос меню"), unknownQuery);
	}
}
