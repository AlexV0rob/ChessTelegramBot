package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для реализации логики перемещения коня
 */
class Knight implements Chessmen {
	@Override
    public boolean checkMove(int rawStartPos, int rawEndPos, byte[] chessDesk, boolean isWhite,
                    PositionConverter positionConverter){
        if (chessDesk[rawEndPos] == 0 || (chessDesk[rawEndPos] > 0) != isWhite) {
            int startPosRow = positionConverter.positionRow(rawStartPos);
            int startPosColumn = positionConverter.positionColumn(rawStartPos);
            int endPosRow = positionConverter.positionRow(rawEndPos);
            int endPosColumn = positionConverter.positionColumn(rawEndPos);
            if((Math.abs(startPosRow - endPosRow) == 2 && Math.abs(endPosColumn - startPosColumn) == 1) 
            		||(Math.abs(startPosRow - endPosRow) == 1 && Math.abs(endPosColumn - startPosColumn) == 2))
            	return true;
        }
		return false;
	}
	@Override
    public List<Integer> everyPossibleMove(int rawStartPos, byte[] chessDesk,
            boolean isWhite, PositionConverter positionConverter) {
        List<Integer> possibleMoves = new ArrayList<>();
        int RowStarPosition = positionConverter.positionRow(rawStartPos);
        //Позиция, движущаяся по доске вертикально вверх
        int UpAndLeftPosition = rawStartPos;
        //Позиция, движущаяся по доске вертикально вниз
        int UpAndRightPosition = rawStartPos;
        //Позиция, движущаяся по доске горизонтально влево
        int DownAndLeftPosition = rawStartPos;
        //Позиция, движущаяся по доске горизонтально вправо
        int DownAndRightPosition= rawStartPos;
        //Смещения позиций
        UpAndLeftPosition = positionConverter.nSquaresUpFromPositionX(2, UpAndLeftPosition);
        UpAndLeftPosition = positionConverter.nSquaresLeftFromPositionX(1, UpAndLeftPosition); 
        
        UpAndRightPosition = positionConverter.nSquaresUpFromPositionX(2, UpAndRightPosition);
        UpAndRightPosition = positionConverter.nSquaresRightFromPositionX(1, UpAndLeftPosition);
        
        DownAndLeftPosition = positionConverter.nSquaresDownFromPositionX(2, DownAndLeftPosition);
        DownAndLeftPosition = positionConverter.nSquaresLeftFromPositionX(1, DownAndLeftPosition); 
        
        DownAndRightPosition = positionConverter.nSquaresDownFromPositionX(2, DownAndRightPosition);
        DownAndRightPosition = positionConverter.nSquaresRightFromPositionX(1, DownAndRightPosition); 
        if(UpAndLeftPosition >= 0 && chessDesk[UpAndLeftPosition] == 0){
        	possibleMoves.add(UpAndLeftPosition);
        }
        if(UpAndRightPosition >= 0 && chessDesk[UpAndRightPosition] == 0){
        	possibleMoves.add(UpAndRightPosition);
        }
        if(DownAndLeftPosition >= 0 && chessDesk[DownAndLeftPosition] == 0){
        	possibleMoves.add(DownAndLeftPosition);
        }
        if(DownAndRightPosition >= 0 && chessDesk[DownAndRightPosition] == 0){
        	possibleMoves.add(DownAndRightPosition);
        }
        UpAndLeftPosition = positionConverter.nSquaresDownFromPositionX(1, UpAndLeftPosition);
        UpAndLeftPosition = positionConverter.nSquaresLeftFromPositionX(1, UpAndLeftPosition); 
        
        UpAndRightPosition = positionConverter.nSquaresDownFromPositionX(1, UpAndRightPosition);
        UpAndRightPosition = positionConverter.nSquaresRightFromPositionX(1, UpAndLeftPosition);
        
        DownAndLeftPosition = positionConverter.nSquaresUpFromPositionX(1, DownAndLeftPosition);
        DownAndLeftPosition = positionConverter.nSquaresLeftFromPositionX(1, DownAndLeftPosition); 
        
        DownAndRightPosition = positionConverter.nSquaresUpFromPositionX(1, DownAndRightPosition);
        DownAndRightPosition = positionConverter.nSquaresRightFromPositionX(1, DownAndRightPosition);
        if(UpAndLeftPosition >= 0 && chessDesk[UpAndLeftPosition] == 0){
        	possibleMoves.add(UpAndLeftPosition);
        }
        if(UpAndRightPosition >= 0 && chessDesk[UpAndRightPosition] == 0){
        	possibleMoves.add(UpAndRightPosition);
        }
        if(DownAndLeftPosition >= 0 && chessDesk[DownAndLeftPosition] == 0){
        	possibleMoves.add(DownAndLeftPosition);
        }
        if(DownAndRightPosition >= 0 && chessDesk[DownAndRightPosition] == 0){
        	possibleMoves.add(DownAndRightPosition);
        }        
        return possibleMoves;
	}
}