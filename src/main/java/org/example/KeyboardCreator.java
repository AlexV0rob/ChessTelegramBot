package org.example;

import java.util.List;

import org.example.buttons.*;

/**
 * Создать клавиатуру
 */
public class KeyboardCreator {
	/**
	 * Клавиатура в меню - обычная Reply клавиатура, поэтому требует
	 * обычных кнопок
	 */
	public List<SimpleButton> menuButtons() {
		return List.of(new SimpleButton("Начать игру на этом устройстве"));
	}
	
	/**
	 * Клавиатура во время игры - Inline клавиатура из кнопок с callback
	 * данными, поэтому использованы идентифицированные кнопки
	 */
	public List<IdentificatedButton> gameButtons(GameState currentGameState) {
		return List.copyOf(currentGameState.nextButtons());
	}
}
