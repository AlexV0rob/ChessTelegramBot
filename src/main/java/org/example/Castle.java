package org.example;
/**
 * Класс для реализации логики перемещения Ладьи
 */
class Castle implements  Chessmen{
	public boolean CheckMove(int rawStartPos, int rawEndPos,byte[] chessDesk, byte isBlack) {
		if((chessDesk[rawEndPos] == 0 ) || (chessDesk[rawEndPos] % 2 != isBlack)){
			Position endPos= new Position(rawEndPos);
			Position startPos= new Position(rawStartPos);
			if(((startPos.line == endPos.line) ^ (startPos.column == endPos.column)) && IsThereObstacle(rawStartPos, rawEndPos,chessDesk) )
				return true;
		}
		return false;
	}
	public boolean IsThereObstacle(int rawStartPos, int rawEndPos,byte[] chessDesk){
		 if( Math.abs(rawEndPos - rawEndPos) <= 7 ){
			 if(rawEndPos > rawStartPos){
			 for(int i = rawStartPos; i < rawEndPos; ++i)
				 if(chessDesk[i] != 0)
					 return false;
			 }
			 else{
				 for(int i = rawStartPos; i > rawEndPos; --i)
					 if(chessDesk[i] != 0)
						 return false; 
			 }
		 }
		 else
		 {
			 if(rawEndPos > rawStartPos){
			 for(int i = rawStartPos; i < rawEndPos; i+=8)
				 if(chessDesk[i] != 0)
					 return false;
			 }
			 else{
				 for(int i = rawStartPos; i > rawEndPos; i-=8)
					 if(chessDesk[i] != 0)
						 return false; 
			 } 
		 }
		 
		 return true;
	 }
}
