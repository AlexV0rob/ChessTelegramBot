package org.example.states;

import org.example.chess.PositionOnBoard;

/**
 * Хранитель состояния игры
 */
public class GameState {
	/**
     * Начальная доска
     */
    private final static byte[][] START_BOARD =
            {
                    {-2, -3, -4, -5, -6, -4, -3, -2},
                    {-1, -1, -1, -1, -1, -1, -1, -1},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {1, 1, 1, 1, 1, 1, 1, 1},
                    {2, 3, 4, 5, 6, 4, 3, 2}
            };
    
    /**
     * Длина стороны доски
     */
    private final static int SIDE_LENGTH = 8;

	/**
	 * Доска
	 */
	private final byte[][] chessboard;
	
	/**
	 * Индикатор ходящей стороны
	 */
	private boolean whiteToMove;
	
	/**
	 * Минимальный конструктор для создания доски, готовой к игре
	 */
	public GameState() {
		this(START_BOARD, SIDE_LENGTH, true);
	}
	
	/**
	 * Полный конструктор
	 */
	public GameState(byte[][] startChessboard, int boardSideLength, boolean doesWhiteStart) {
		chessboard = new byte[boardSideLength][boardSideLength];
		for (int i = 0; i < boardSideLength; ++i) {
			for (int j = 0; j < boardSideLength; ++j) {
				chessboard[i][j] = startChessboard[i][j];
			}
		}
		whiteToMove = doesWhiteStart;
	}
	
	/**
	 * Передвинуть фигуру и поставить 0 в стартовую позицию
	 */
	public void moveFigure(PositionOnBoard start, PositionOnBoard finish) {
		chessboard[finish.row()][finish.column()] = chessboard[start.row()][start.column()];
		chessboard[start.row()][start.column()] = 0;
	}
	
	/**
	 * Поменять ходящую сторону
	 */
	public void changeSide() {
		whiteToMove = !whiteToMove;
	}
	
	/**
	 * Получить текущую доску
	 */
	public byte[][] getBoard() {
		return chessboard;
	}
	
	/**
	 * Ходят ли сейчас белые
	 */
	public boolean isWhiteToMove() {
		return whiteToMove;
	}
}
