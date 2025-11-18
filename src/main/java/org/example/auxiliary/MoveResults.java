package org.example.auxiliary;

import java.util.List;

/**
 * Возможные результаты хода для более высокоуровневой обработки
 */
public record MoveResults(moveStatus thisMoveStatus, 
		List<List<String>> messagesTextsLists) {
	/**
	 * Возможные результаты хода
	 */
	public enum moveStatus {
		/**
		 * Совершить ход не удалось
		 */
		FAILURE,
		/**
		 * Ход был совершён
		 */
		SUCCESS,
		/**
		 * Игра завершена
		 */
		GAMEOVER
	}
}
