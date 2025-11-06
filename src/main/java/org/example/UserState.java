package org.example;

public class UserState {
	private final static byte[][] START_BOARD = 
		{
				{-2, -3, -4, -5, -6, -4, -3, -2},
				{-1, -1, -1, -1, -1, -1, -1, -1},
				{ 0,  0,  0,  0,  0,  0,  0,  0},
				{ 0,  0,  0,  0,  0,  0,  0,  0},
				{ 0,  0,  0,  0,  0,  0,  0,  0},
				{ 0,  0,  0,  0,  0,  0,  0,  0},
				{ 1,  1,  1,  1,  1,  1,  1,  1},
				{ 2,  3,  4,  5,  6,  4,  3,  2},
		};
	
	public enum USER_STATE {
		MAINMENU,
		INGAME
	}
	
	private final static int BOARD_SIDE_LENGTH = 8;
	
	private USER_STATE currentUserState;
	
	private GameState currentGameState;
	
	private final MoveState currentMoveState;
	
	public UserState() {
		currentGameState = new GameState(START_BOARD, BOARD_SIDE_LENGTH, true);
		currentUserState = USER_STATE.MAINMENU;
		currentMoveState = new MoveState();
	}
	
	public void setUserState(USER_STATE newUserState) {
		currentUserState = newUserState;
	}
	
	public void resetGameState() {
		currentGameState = new GameState(START_BOARD, BOARD_SIDE_LENGTH, true);		
	}
	
	public USER_STATE getUserState() {
		return currentUserState;
	}
	
	public GameState getGameState() {
		return currentGameState;
	}
	
	public MoveState getMoveState() {
		return currentMoveState;
	}
}
