package heuristic;

import game2048.Tile;

public class BePair implements Heuristic {

	@Override
	public double function(Tile[] board) {
		int table[] = new int[15];

		for (int i = 0; i < 15; i++) {
			table[i] = 0;
		}

		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				board[x * 4 + y].setCol(x);
				board[x * 4 + y].setRow(y);
			}
		}

		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				for (int i = 0; i < 4 - x; i++) {
					if (board[(x + i) * 4 + y].getValue().intValue() == 0)
						continue;
					else { 
						if (board[x * 4 + y].getValue().intValue() == board[(x + i) * 4 + y].getValue().intValue())
							table[LogNumber.LogReturn(board[x * 4 + y].getValue())]++;
						break;
					}
				}
				for (int i = 0; i < 4 - x; i++) {
					if (board[(x) * 4 + y + i].getValue().intValue() == 0)
						continue;
					else { 
						if (board[x * 4 + y].getValue().intValue() == board[(x) * 4 + y + i].getValue().intValue())
							table[LogNumber.LogReturn(board[x * 4 + y].getValue())]++;
						break;
					}
				}
				
					
			}
		}

		double score = 0;
		for (int i = 0; i < 15; i++) {
			score += table[i] * i;
		}

		return score;
	}

}
