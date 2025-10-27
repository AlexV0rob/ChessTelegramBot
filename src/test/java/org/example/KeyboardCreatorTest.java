package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import org.example.buttons.*;

/**
 * Проверка создателя клавиатур
 */
public class KeyboardCreatorTest {
	/**
	 * Экземпляр создателя клавиатур
	 */
	private final KeyboardCreator keyboardCreator = new KeyboardCreator();
	
	/**
	 * Проверка клавиатуры в меню
	 */
	@Test
	void menuKeyboardTest() {
		Assertions.assertIterableEquals(
				List.of(new SimpleButton("Начать игру на этом устройстве")),
				keyboardCreator.menuButtons());
	}
	
	/**
	 * Проверка клавиатуры в игре
	 */
	@Test
	void gameKeyboardTest() {
		GameState gameState = new GameState();
		gameState.setBoard(new byte[] {
				0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, -1
		});
		Assertions.assertIterableEquals(
				List.of(new IdentificatedButton("ПЕШКА", String.valueOf((char) 1))),
				keyboardCreator.gameButtons(gameState));
	}
}
