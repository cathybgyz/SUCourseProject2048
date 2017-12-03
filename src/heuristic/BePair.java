package heuristic;

import game2048.Tile;

public class BePair implements Heuristic {

	@Override
	public double function(Tile[] board) {
		int table[] = new int[15];
		
		for(int i=0;i<15;i++) {
			table[i] = 0;
		}
		
		for (int x=0;x<4;x++){
			for(int y=0;y<4;y++) {
				board[x*4+y].setCol(x);
				board[x*4+y].setRow(y);
			}
		}
		
		for(int x=0;x<3;x++) {
			for(int y=0;y<3;y++) {
				if(board[x*4+y].getValue() == board[(x+1)*4+y].getValue())
					table[LogNumber.LogReturn(board[x*4+y].getValue())]++;
				else if(board[x*4+y].getValue() == board[(x)*4+y+1].getValue())
					table[LogNumber.LogReturn(board[x*4+y].getValue())]++;
			}
		}
		
		double score = 0;
		for(int i=0;i<15;i++) {
			score += table[i]*i;
		}
		
		return score;
	}

}
