package org.example.auxiliary;

import java.util.List;

/**
 * Результаты обработки команды для более высокоуровневой обработки
 */
public record CommandResults(lobbyStatus userLobbyStatus, 
		List<List<String>> messagesTextsLists) {
	/**
	 * Состояние относительно матча после обработки команды
	 */
	public enum lobbyStatus {
		/**
		 * С матчем ничего делать не нужно
		 */
		NOTHING,
		/**
		 * Создать однопользовательский матч
		 */
		SINGLEPLAYER,
		/**
		 * Создать многопользовательский матч
		 */
		MULTIPLAYER,
		/**
		 * Завершить матч
		 */
		CLOSE
	}
}
