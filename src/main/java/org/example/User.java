package org.example;

/**
 * Сущность пользователя, хранит информацию о текущем состоянии чата
 * с конкретным пользователем
 */
public class User {
	/**
	 * Режим, в котором сейчас работает бот
	 */
	private byte mode;
	/**
	 * Состояние доски, список фигур по кодам:
	 * 0 - пустое поле
	 * 1 - чёрная пешка
	 * 2 - белая пешка
	 * 3 - чёрная ладья
	 * 4 - белая ладья
	 * 5 - чёрный конь
	 * 6 - белый конь
	 * 7 - чёрный слон
	 * 8 - белый слон
	 * 9 - чёрный ферзь
	 * 10 - белый ферзь
	 * 11 - чёрный король
	 * 12 - белый король
	 */
	private byte[] board;
	/**
	 * true, если сейчас ходят белые
	 */
	private boolean whitesMove;
	
	/**
	 * Конструктор, изначально режим 0 (главное меню) и пустая доска
	 */
	public User() {
		mode = 0;
		board = new byte[64];
		whitesMove = true;
	}
	
	/**
	 * Поменять режим
	 * @param newMode
	 */
	public void changeMode(byte newMode) {
		mode = newMode;
	}
	
	/**
	 * Сформировать доску для новой партии
	 * Белые "снизу" (в конце массива)
	 */
	public void newBoard() {
		board = new byte[] {3, 5, 7, 9, 11, 7, 5, 3,
							1, 1, 1, 1, 1, 1, 1, 1,
							0, 0, 0, 0, 0, 0, 0, 0,
							0, 0, 0, 0, 0, 0, 0, 0,
							0, 0, 0, 0, 0, 0, 0, 0,
							0, 0, 0, 0, 0, 0, 0, 0,
							2, 2, 2, 2, 2, 2, 2, 2,
							4, 6, 8, 10, 12, 8, 6, 4};
	}
	
	/**
	 * Поменять ходящую сторону
	 */
	public void changeSide() {
		whitesMove = !whitesMove;
	}
	
	/**
	 * Получить текущий режим
	 * @return
	 */
	public byte getMode() {
		return mode;
	}
	
	/**
	 * Получить текущее состояние доски
	 * @return
	 */
	public byte[] getBoard() {
		return board;
	}
	
	/**
	 * Узнать, кто сейчас ходит
	 */
	public boolean doesWhitesMove() {
		return whitesMove;
	}
}
