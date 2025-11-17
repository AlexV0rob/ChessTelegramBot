package org.example.states;

/**
 * Состояние матча
 */
public class LobbyState {
	/**
	 * Возможные состояния матча
	 */
	public enum LobbyType {
		/**
		 * Одиночная игра
		 */
		SINGLEPLAYER,
		/**
		 * Многопользовательская игра
		 */
		MULTIPLAYER
	}
	
	/**
	 * Состояние игры
	 */
	private final GameState gameState = new GameState();
	
	/**
	 * Идентификатор первого пользователя
	 */
	private final long firstId;

	/**
	 * Идентификатор второго пользователя
	 */
	private long secondId = 0;
	
	/**
	 * Сторона первого пользователя
	 */
	private final boolean isFirstWhite;
	
	/**
	 * Тип матча
	 */
	private final LobbyType type;
	
	/**
	 * Конструктор
	 */
	public LobbyState(long firstPlayerId, LobbyType thisLobbyType) {
		firstId = firstPlayerId;
		isFirstWhite = true;
		type = thisLobbyType;
	}
	
	/**
	 * Установить идентификатор фторого пользователя
	 */
	public void setSecondPlayerId(long playerId) {
		if (secondId == 0) {
			secondId = playerId;
		}
	}
	
	/**
	 * Получить состояние игры
	 */
	public GameState getGameState() {
		return gameState;
	}
	
	/**
	 * Получить идентификатор первого пользователя
	 */
	public long getFirstPlayerId() {
		return firstId;
	}
	
	/**
	 * Получить идентификатор второго пользователя
	 */
	public long getSecondPlayerId() {
		return secondId;
	}
	
	/**
	 * Получить сторону первого игрока
	 */
	public boolean isFirstPlayerWhite() {
		return isFirstWhite;
	}
	
	/**
	 * Получить сторону второго игрока
	 */
	public boolean isSecondPlayerWhite() {
		return !isFirstWhite;
	}
	
	/**
	 * Получить тип матча
	 */
	public LobbyType getLobbyType() {
		return type;
	}
}
