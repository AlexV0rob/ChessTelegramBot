package org.example;
/**
 * Класс для реализации логики перемещения Ладьи
 */
class Castle implements  Chessmen{
	public boolean checkMove(int rawStartPos, int rawEndPos,byte[] chessDesk, boolean isWhite) {
		if((rawStartPos > 63 || rawStartPos < 0 ) || (rawEndPos > 63 || rawEndPos < 0))
			return false;
		if((chessDesk[rawEndPos] == 0 ) || ((chessDesk[rawEndPos] % 2 == 0 ) != isWhite)){
			Position endPos= new Position(rawEndPos);
			Position startPos= new Position(rawStartPos);
			if(((startPos.line == endPos.line) ^ (startPos.column == endPos.column)))
				return true;
		}
		return false;
	}
	public boolean IsThereObstacle(int rawStartPos, int rawEndPos,byte[] chessDesk){
		// если разница меньше 7, то они на одной линии
		 if( Math.abs(rawEndPos - rawStartPos) <= 7 ){

			 if(rawEndPos > rawStartPos){
			 for(int i = rawStartPos + 1; i < rawEndPos; ++i)
				 if(chessDesk[i] != 0)
					 return false;
			 }
			 else{
				 for(int i = rawStartPos - 1; i > rawEndPos; --i)
					 if(chessDesk[i] != 0)
						 return false; 
			 }
		 }
		 else
		 {
			 if(rawEndPos > rawStartPos){
			 for(int i = rawStartPos+8; i < rawEndPos; i+=8)
				 if(chessDesk[i] != 0)
					 return false;
			 }
			 else{
				 for(int i = rawStartPos - 8; i > rawEndPos; i-=8)
					 if(chessDesk[i] != 0)
						 return false; 
			 } 
		 }
		 
		 return true;
	 }
}
