package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для реализации логики перемещения Короля
 */
class King implements Chessmen {
	@Override
	public boolean checkMove(int rawStartPos, int rawEndPos, byte[] chessDesk, boolean isWhite,
			PositionConverter positionConverter) {
        if(chessDesk[rawEndPos] == 0 || (chessDesk[rawEndPos] > 0) != isWhite) {
            int startPosRow = positionConverter.positionRow(rawStartPos);
            int startPosColumn = positionConverter.positionColumn(rawStartPos);
            int endPosRow = positionConverter.positionRow(rawEndPos);
            int endPosColumn = positionConverter.positionColumn(rawEndPos);
            if(Math.abs(endPosRow - startPosRow) <= 1 && Math.abs(endPosColumn - startPosColumn) <= 1)
            	return true;
        }
        return false;
	}
	@Override
	public List<Integer> everyPossibleMove(int rawStartPos, byte[] chessDesk, boolean isWhite,
			PositionConverter positionConverter) {
        List<Integer> possibleMoves = new ArrayList<>();
        // позиция, передвигаемая против часовой снизу вверх по потенциальным ходам Короля
        int UpAndLeftPosition = rawStartPos;
        // позиция, передвигаемая по часовой свурху вниз потенциальным ходам Короля
        int DownAndRightPosition = rawStartPos;
        //сдвинем позиции на стартовые позиции
        UpAndLeftPosition = positionConverter.nSquaresDownFromPositionX(1, UpAndLeftPosition);
        UpAndLeftPosition = positionConverter.nSquaresRightFromPositionX(1, UpAndLeftPosition);

        DownAndRightPosition = positionConverter.nSquaresUpFromPositionX(1, DownAndRightPosition);
        DownAndRightPosition = positionConverter.nSquaresLeftFromPositionX(1, DownAndRightPosition);
        for(int i = 0; i < 2; ++i)
        {
        	if(UpAndLeftPosition >= 0 && 
        			(chessDesk[UpAndLeftPosition] == 0 || (chessDesk[UpAndLeftPosition] > 0) != isWhite)) {
        		possibleMoves.add(UpAndLeftPosition);
        		UpAndLeftPosition = positionConverter.nSquaresUpFromPositionX(1, UpAndLeftPosition);

        	}
        	if(DownAndRightPosition >= 0 && 
        			(chessDesk[DownAndRightPosition] == 0 || (chessDesk[DownAndRightPosition] > 0) != isWhite)) {
        		possibleMoves.add(DownAndRightPosition);
        		DownAndRightPosition = positionConverter.nSquaresDownFromPositionX(1, DownAndRightPosition);
        	}
        }
        UpAndLeftPosition = positionConverter.nSquaresLeftFromPositionX(1, UpAndLeftPosition);
        DownAndRightPosition = positionConverter.nSquaresRightFromPositionX(1, DownAndRightPosition);

        for(int i = 0; i < 2; ++i)
        {
        	if(UpAndLeftPosition >= 0 && 
        			(chessDesk[UpAndLeftPosition] == 0 || (chessDesk[UpAndLeftPosition] > 0) != isWhite)) {
        		possibleMoves.add(UpAndLeftPosition);
        		UpAndLeftPosition = positionConverter.nSquaresLeftFromPositionX(1, UpAndLeftPosition);

        	}
        	if(DownAndRightPosition >= 0 && 
        			(chessDesk[DownAndRightPosition] == 0 || (chessDesk[DownAndRightPosition] > 0) != isWhite)) {
        		possibleMoves.add(DownAndRightPosition);
        		DownAndRightPosition = positionConverter.nSquaresRightFromPositionX(1, DownAndRightPosition);
        	}
        }
		return possibleMoves;
	}
}