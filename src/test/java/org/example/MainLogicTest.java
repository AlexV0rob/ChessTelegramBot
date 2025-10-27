package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Проверка работы основного обработчика пользовательского ввода
 */
public class MainLogicTest {
	/**
	 * Основной логический обработчик ввода
	 */
	private final MainLogic mainLogic = new MainLogic();

	/**
	 * Болванка с игровым состоянием
	 */
	private final GameState gameState = new GameState();
	
	/**
	 * Проверка ввода команды
	 */
	@Test
	void commandInputTest() {
		
	}
}
