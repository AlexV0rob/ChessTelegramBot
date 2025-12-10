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
		userState.setUserState(UserState.userStatus.MAIN_MENU);
		mainMenuInputHandler.processInput(
				"Начать игру на этом устройстве", userState);
		Assertions.assertEquals(userState.getUserState(), UserState.userStatus.IN_GAME);
	}
	
	/**
	 * Проверка неизвестного запроса
	 */
	@Test
	public void unknownQueryTest() {
		UserState userState = new UserState();
		userState.setUserState(UserState.userStatus.MAIN_MENU);
		List<String> unknownQuery = mainMenuInputHandler.processInput(
				"Какой-то запрос", userState);
		Assertions.assertEquals(
				userState.getUserState(), UserState.userStatus.MAIN_MENU);
		Assertions.assertIterableEquals(
				List.of("Неизвестный запрос меню"), unknownQuery);
	}
}
