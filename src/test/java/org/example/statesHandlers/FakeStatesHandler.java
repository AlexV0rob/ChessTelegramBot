package org.example.statesHandlers;

import org.example.states.UserState;

/**
 * Тестовый хранитель состояний
 */
public class FakeStatesHandler extends MemoryStatesHandler {
	/**
	 * Добавить пользователя с определённым состоянием
	 */
	public void addNewUserWithStatus(long userId, UserState.UserStatus status) {
		users.put(userId, null);
		users.get(userId).setUserState(status);
	}
	
	/**
	 * Сбросить все данные
	 */
	public void resetAll() {
		users.clear();
		games.clear();
		names.clear();
		messages.clear();
	}
}
