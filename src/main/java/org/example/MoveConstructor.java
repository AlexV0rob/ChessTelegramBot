package org.example;

/**
 * На каком этапе конструирования находится ход игрока
 */
public class MoveConstructor {
	public static enum STATUS {
		NOTHING,
		FIGURE,
		START,
		FINISH
	}
	private byte figure;
	private int startPosition;
	private int finishPosition;
	private STATUS currentStatus;
	
	public MoveConstructor() {
		figure = 0;
		startPosition = -1;
		finishPosition = -1;
		currentStatus= STATUS.NOTHING;
	}
	
	public void nextStatus(int newPart) {
		switch (currentStatus) {
		case NOTHING:
			figure = (byte) newPart;
			currentStatus = STATUS.FIGURE;
			break;
		case FIGURE:
			startPosition = newPart;
			currentStatus = STATUS.START;
			break;
		case START:
			finishPosition = newPart;
			currentStatus = STATUS.FINISH;
			break;
		case FINISH:
			figure = (byte) 0;
			startPosition = -1;
			finishPosition = -1;
			currentStatus = STATUS.NOTHING;
			break;
		}
	}
	
	public STATUS getStatus() {
		return currentStatus;
	}
	
	public byte getFigure() {
		return figure;
	}
	
	public int getStartPosition() {
		return startPosition;
	}
	
	public int getFinishPosition() {
		return finishPosition;
	}
	
	public String flushMove() {
		String move = String.valueOf((char) figure) +
				String.valueOf((char) startPosition) + 
				String.valueOf((char) finishPosition);
		nextStatus(0);
		return move;
	}
}
