package org.example;

public class Bishop implements  Chessmen{

	public boolean CheckMove(int rawStartPos, int rawEndPos,byte[] chessDesk, byte isBlack) {
		/* Проверяем правильность хода в два этапа:
		  1) Смотрим что интересующая нас клетка нас не занята или там находится шахматная фигура оппонента
		  2) Проверяем что слон может так сходить
		 * */
		
		 if ((chessDesk[rawEndPos] == 0 ) || (chessDesk[rawEndPos] % 2 != isBlack) ){  
			Position endPos= new Position(rawEndPos);
			Position startPos= new Position(rawStartPos);
			 if(Math.abs(endPos.line-startPos.line)==Math.abs(startPos.column-endPos.column))
				 return true;
		 }
		return false;
	} 
}