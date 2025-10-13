package org.example;
/**
 * Класс для реализации логики перемещения слона
 */
public class Bishop implements  Chessmen{

	public boolean CheckMove(int rawStartPos, int rawEndPos,byte[] chessDesk, boolean isWhite) {
		/* Проверяем правильность хода в два этапа:
		  1) Смотрим что интересующая нас клетка нас не занята или там находится шахматная фигура оппонента
		  2) Проверяем что слон может так сходить
		 * */
		
		 if (((chessDesk[rawEndPos] == 0 ) || ((chessDesk[rawEndPos] % 2 != 0 ) != isWhite))){  
			Position endPos= new Position(rawEndPos);
			Position startPos= new Position(rawStartPos);
			 if(Math.abs(endPos.line-startPos.line)==Math.abs(startPos.column-endPos.column))
				 return true;
		 }
		return false;
	}
	//Проверяем что на пути у нашего слона нету преград
	 public boolean IsThereObstacle(int rawStartPos, int rawEndPos,byte[] chessDesk){
		if(rawEndPos > rawStartPos) {
			if((rawEndPos-rawStartPos) % 7 == 0) {
					 for(int i = rawStartPos; i < rawEndPos; i+=7)
						 if(chessDesk[i] != 0)
							 return false;
			}
			else {
				 for(int i = rawStartPos; i < rawEndPos; i+=9)
					 if(chessDesk[i] != 0)
						 return false;	
			}	
		}
		else {
			if((rawStartPos-rawEndPos) % 7 == 0) {
				 if(rawEndPos > rawStartPos){
					 for(int i = rawEndPos; i < rawStartPos; i+=7)
						 if(chessDesk[i] != 0)
							 return false;
					 }
			}
			else {
				 for(int i = rawEndPos; i < rawStartPos; i+=9)
					 if(chessDesk[i] != 0)
						 return false;
			}
		}
		 return true;
	 }
}