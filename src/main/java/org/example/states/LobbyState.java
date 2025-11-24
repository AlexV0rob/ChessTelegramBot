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
	private boolean isFirstMove;
	
	/**
	 * Тип матча
	 */
	private final LobbyType type;
	
	/**
	 * Конструктор
	 */
	public LobbyState(long firstPlayerId, LobbyType thisLobbyType) {
		firstId = firstPlayerId;
		isFirstMove = true;
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
	 * Поменять ходящего игрока
	 */
	public void changeMovingPlayer() {
		isFirstMove = !isFirstMove;
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
	 * Ходит ли первый игрок
	 */
	public boolean isFirstPlayerToMove() {
		return isFirstMove;
	}
	
	/**
	 * Получить тип матча
	 */
	public LobbyType getLobbyType() {
		return type;
	}
	
	/**
	 * Получить идентификатор другого игрока
	 */
	public long getAnotherPlayerId(long thatId) {
		return thatId == firstId ? secondId : firstId;
	}
}
