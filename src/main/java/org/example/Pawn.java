package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для реализации логики перемещения пешки
 */
public class Pawn implements Chessmen {
	/**
	 * линия, с которой стартуют белые пешки
	 */
	static int WHITE_PAWN_START_ROW = 1;
	/**
	 * линия, с которой стартуют чёрные пешки
	 */
	static int BLACK_PAWN_START_ROW = 6;

	@Override
	public boolean checkMove(int rawStartPos, int rawEndPos, byte[] chessDesk, boolean isWhite,
			PositionConverter positionConverter) {
        int startPosRow = positionConverter.positionRow(rawStartPos);
        int startPosColumn = positionConverter.positionColumn(rawStartPos);
        int endPosRow = positionConverter.positionRow(rawEndPos);
        int endPosColumn = positionConverter.positionColumn(rawEndPos);
		if(chessDesk[rawEndPos] == 0 && endPosRow - startPosRow  == 1 * (isWhite ? 1 : -1))
			return true;
		else if(chessDesk[rawEndPos] == 0 
				&& (WHITE_PAWN_START_ROW == startPosRow ||BLACK_PAWN_START_ROW == startPosRow  ) 
				&& endPosRow - startPosRow  == 1 * (isWhite ? 2 : -2))
			return true;
		else if((chessDesk[rawEndPos] > 0) != isWhite  && 
				endPosRow - startPosRow  == 1 * (isWhite ? 1 : -1) 
				&& Math.abs(startPosColumn - endPosColumn) == 1)
			return true;
		return false;
	}

	@Override
	public List<Integer> everyPossibleMove(int rawStartPos, byte[] chessDesk, boolean isWhite,
			PositionConverter positionConverter) {
	        List<Integer> possibleMoves = new ArrayList<>();
	        int startPosRow = positionConverter.positionRow(rawStartPos);
	        if(chessDesk[rawStartPos] > 0) {
	        	//единичный ход пешки
	        	int OneTimeUpPos = rawStartPos;
	        	//двойной ход со стартовой позиции
	        	int TwoTimesUpPos = rawStartPos;
	        	//ход, рубящий по диагонали вправо
	        	int RightAndUpPos = rawStartPos;
	        	//ход, рубящий по диагонали влево
	        	int LeftAndUpPos = rawStartPos;
	        	// сдвигаем на позиции наши ходы
	        	OneTimeUpPos = positionConverter.nSquaresUpFromPositionX(1, OneTimeUpPos);
	        	
	        	TwoTimesUpPos = positionConverter.nSquaresUpFromPositionX(2, TwoTimesUpPos);
	        	int TwoTimesUpRow = positionConverter.positionRow(TwoTimesUpPos);
	        	RightAndUpPos = positionConverter.nSquaresUpFromPositionX(1, RightAndUpPos);
	        	RightAndUpPos = positionConverter.nSquaresRightFromPositionX(1, RightAndUpPos);
	        	
	        	LeftAndUpPos = positionConverter.nSquaresUpFromPositionX(1, LeftAndUpPos);
	        	LeftAndUpPos = positionConverter.nSquaresRightFromPositionX(1, LeftAndUpPos);
	        	if(OneTimeUpPos >= 0 && chessDesk[OneTimeUpPos] == 0)
	        		possibleMoves.add(OneTimeUpPos);
	        	if(TwoTimesUpPos >= 0 && chessDesk[TwoTimesUpPos] == 0 &&
	        			(TwoTimesUpRow == WHITE_PAWN_START_ROW 
	        			|| TwoTimesUpRow == BLACK_PAWN_START_ROW))
	        		possibleMoves.add(TwoTimesUpPos);
	        	if(RightAndUpPos >= 0 && chessDesk[RightAndUpPos] != 0)
	        		possibleMoves.add(RightAndUpPos);
	        	if(LeftAndUpPos >= 0 && chessDesk[LeftAndUpPos] != 0)
	        		possibleMoves.add(LeftAndUpPos);
	        }	
	        else{
	        	//единичный ход пешки
	        	int OneTimeDownPos = rawStartPos;
	        	//двойной ход со стартовой позиции
	        	int TwoTimesDownPos = rawStartPos;
	        	//ход, рубящий по диагонали вправо
	        	int RightAndDownPos = rawStartPos;
	        	//ход, рубящий по диагонали влево
	        	int LeftAndDownPos = rawStartPos;
	        	// сдвигаем на позиции наши ходы
	        	OneTimeDownPos = positionConverter.nSquaresUpFromPositionX(1, OneTimeDownPos);
	        	
	        	TwoTimesDownPos = positionConverter.nSquaresUpFromPositionX(2, TwoTimesDownPos);
	        	int TwoTimesDownRow = positionConverter.positionRow(TwoTimesDownPos);
	        	RightAndDownPos = positionConverter.nSquaresUpFromPositionX(1, RightAndDownPos);
	        	RightAndDownPos = positionConverter.nSquaresRightFromPositionX(1, RightAndDownPos);
	        	
	        	LeftAndDownPos = positionConverter.nSquaresUpFromPositionX(1, LeftAndDownPos);
	        	LeftAndDownPos = positionConverter.nSquaresRightFromPositionX(1, LeftAndDownPos);
	        	if(OneTimeDownPos >= 0 && chessDesk[OneTimeDownPos] == 0)
	        		possibleMoves.add(OneTimeDownPos);
	        	if(TwoTimesDownPos >= 0 && chessDesk[TwoTimesDownPos] == 0 &&
	        			(TwoTimesDownRow == WHITE_PAWN_START_ROW 
	        			|| TwoTimesDownRow == BLACK_PAWN_START_ROW))
	        		possibleMoves.add(TwoTimesDownPos);
	        	if(RightAndDownPos >= 0 && chessDesk[RightAndDownPos] != 0)
	        		possibleMoves.add(RightAndDownPos);
	        	if(LeftAndDownPos >= 0 && chessDesk[LeftAndDownPos] != 0)
	        		possibleMoves.add(LeftAndDownPos);
	        }
	        return possibleMoves;
	    }
}
