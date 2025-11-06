package org.example;

public class GameState {
	private byte[][] chessboard;
	private boolean whiteToMove;
	
	public GameState(byte[][] startChessboard, int boardSideLength, boolean doesWhiteStart) {
		chessboard = new byte[boardSideLength][boardSideLength];
		for (int i = 0; i < boardSideLength; ++i) {
			for (int j = 0; j < boardSideLength; ++j) {
				chessboard[i][j] = startChessboard[i][j];
			}
		}
		whiteToMove = doesWhiteStart;
	}
	
	public void moveFigure(PositionOnBoard start, PositionOnBoard finish) {
		chessboard[finish.row()][finish.column()] = chessboard[start.row()][start.column()];
		chessboard[start.row()][start.column()] = 0;
	}
	
	public void changeSide() {
		whiteToMove = !whiteToMove;
	}
	
	public byte[][] getBoard() {
		return chessboard;
	}
	
	public boolean isWhiteToMove() {
		return whiteToMove;
	}
}
