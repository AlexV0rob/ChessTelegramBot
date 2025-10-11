package org.example;

public interface Chessmen {
	boolean checkMove(Position startPos, Position endPos,byte[][] chessDesk);
	void Move(Position startPos, Position endPos,byte[][] chessDesk);
}