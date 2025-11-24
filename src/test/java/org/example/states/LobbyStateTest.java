package org.example.states;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * Проверка хранителя состояния матча
 */
public class LobbyStateTest {
	/**
	 * Проверить неизменность второго идентификатора после присваивания
	 */
	@Test
	public void secondIdTest() {
		LobbyState lobbyState = 
				new LobbyState(1, 2, true, LobbyState.LobbyType.SINGLEPLAYER);
		Assertions.assertEquals(1, lobbyState.getFirstPlayerId());
		Assertions.assertEquals(2, lobbyState.getSecondPlayerId());
		Assertions.assertTrue(lobbyState.isFirstPlayerToMove());
		Assertions.assertEquals(
				LobbyState.LobbyType.SINGLEPLAYER, 
				lobbyState.getLobbyType());
	}
}
