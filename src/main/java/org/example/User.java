package org.example;

public class User {
	private final long id;
	private byte mode;
	private byte[] board;
	
	public User(long chatId) {
		id = chatId;
		mode = 0;
		board = new byte[64];
	}
	
	public boolean changeMode(byte newMode) {
		mode = newMode;
		
		return true;
	}
	
	//Белые снизу
	public boolean newBoard() {
		board = new byte[] {3, 5, 7, 9, 11, 7, 5, 3,
							1, 1, 1, 1, 1, 1, 1, 1,
							0, 0, 0, 0, 0, 0, 0, 0,
							0, 0, 0, 0, 0, 0, 0, 0,
							0, 0, 0, 0, 0, 0, 0, 0,
							0, 0, 0, 0, 0, 0, 0, 0,
							2, 2, 2, 2, 2, 2, 2, 2,
							4, 6, 8, 10, 12, 8, 6, 4};
		return true;
	}
	
	public long getId() {
		return id;
	}
	
	public byte getMode() {
		return mode;
	}
	
	public byte[] getBoard() {
		return board;
	}
}
