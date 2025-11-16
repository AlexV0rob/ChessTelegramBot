package org.example.states;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * Проверка хранителя пользовательского состояния
 */
public class UserStateTest {
	/**
	 * Храниель пользовательского состояния
	 */
	private final UserState userState = new UserState(null);
	
	/**
	 * Проверка смены режима
	 */
	@Test
	public void modeChangingTest() {
		userState.setUserState(UserState.userState.INGAME);
		Assertions.assertEquals(UserState.userState.INGAME, 
				userState.getUserState());
		userState.setUserState(UserState.userState.MAINMENU);
		Assertions.assertEquals(UserState.userState.MAINMENU, 
				userState.getUserState());
	}
}
