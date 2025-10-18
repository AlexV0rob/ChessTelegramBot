package org.example;
/**
 * Класс для реализации логики перемещения коня
 */
class Knight implements  Chessmen{
	@Override
	public boolean checkMove(int rawStartPos, int rawEndPos,byte[] chessDesk, boolean isWhite) {
		
		if((chessDesk[rawEndPos] == 0 ) || ((chessDesk[rawEndPos] % 2 != 0 ) != isWhite))
		{
			byte[] shifts = {6, 7, 10, 15, 17};
			// проверяем все сдвиги, соотвествующие возможным ходам коня
			for(byte shift : shifts)
			{
				if(rawStartPos + shift < 63)
					if( rawStartPos + shift == rawEndPos)
						return true;
				if(rawStartPos - shift > 0)
					if( rawStartPos - shift == rawEndPos)
						return true;
			}
		}
		return false;
	}
}