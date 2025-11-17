package org.example.auxiliary;

import java.util.List;

/**
 * Возможные результаты хода для более высокоуровневой обработки
 */
public record MoveResults(MoveStatus thisMoveStatus, 
		List<List<String>> messagesTextsLists) {
	/**
	 * Возможные результаты хода
	 */
	public enum MoveStatus {
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
