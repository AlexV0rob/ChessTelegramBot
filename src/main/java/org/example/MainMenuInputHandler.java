package org.example;

public class MainMenuInputHandler implements InputHandler {
	private final static String UNKNOWN_INPUT = "Неизвестный запрос меню"; 

	/**
	 * Строка из меню для начала одиночной игры
	 */
	private final static String NEW_SINGLE_GAME = "Начать игру на этом устройстве";
	
	@Override
	public String processInput(String userInput, UserState currentUserState) {
		if (userInput.equals(NEW_SINGLE_GAME)) {
			currentUserState.setUserState(UserState.USER_STATE.INGAME);
			currentUserState.resetGameState();
			return new GameTranslator().currentBoardState(currentUserState.getGameState());
		}
		return UNKNOWN_INPUT;
	}
}
