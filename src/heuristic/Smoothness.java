package heuristic;

import game2048.Tile;
import heuristic.LogNumber;
public class Smoothness implements Heuristic {
	public double function(Tile[] tile) {
		double total = 0;
		int i;
		int n = tile.length;
		int value[][] = new int[4][4];
		for(i=0;i<n;i++) {
			value[i/4][i%4] = tile[i].getValue();
		}
		for(i=0;i<n;i++) {
			int x = i/4;
			int y = i%4;
			//right
			if(value[x][y] != 0) {
				if(x+1<4) {
					if(value[x][y] == value[x+1][y]) total+=2;
					else if(value[x+1][y] == 0) {
						if(x+2<4 && value[x+2][y] != 0) {
							int log1 = LogNumber.LogReturn(value[x][y]);
							int log2 = LogNumber.LogReturn(value[x+2][y]);
							int logDiff = Math.abs(log1-log2);
							if(logDiff == 0) total +=2;
							else {
								double score = 1.0/logDiff;
								total+=score;
							}
						}
						else if (x+3<4 && value[x+2][y] == 0 && value[x+3][y] != 0) {
							int log1 = LogNumber.LogReturn(value[x][y]);
							int log2 = LogNumber.LogReturn(value[x+3][y]);
							int logDiff = Math.abs(log1-log2);
							if(logDiff == 0) total +=2;
							else {
								double score = 1.0/logDiff;
								total+=score;
							}
						}
					}
					else {
						int log1 = LogNumber.LogReturn(value[x][y]);
						int log2 = LogNumber.LogReturn(value[x+1][y]);
						int logDiff = Math.abs(log1-log2);
						if(logDiff == 0) total +=2;
						else {
							double score = 1.0/logDiff;
							total+=score;
						}
					}
				}
			
			//down
				if(y+1<4) {
					if(value[x][y] == value[x][y+1]) total+=2;
					else if(value[x][y+1] == 0) {
						if(y+2<4 && value[x][y+2] != 0) {
							int log1 = LogNumber.LogReturn(value[x][y]);
							int log2 = LogNumber.LogReturn(value[x][y+2]);
							int logDiff = Math.abs(log1-log2);
							if(logDiff == 0) total +=2;
							else {
								double score = 1.0/logDiff;
								total+=score;
							}
						}
						else if (y+3<4 && value[x][y+2] == 0 && value[x][y+3] != 0) {
							int log1 = LogNumber.LogReturn(value[x][y]);
							int log2 = LogNumber.LogReturn(value[x][y+3]);
							int logDiff = Math.abs(log1-log2);
							if(logDiff == 0) total +=2;
							else {
								double score = 1.0/logDiff;
								total+=score;
							}
						}
					}
					else {
						int log1 = LogNumber.LogReturn(value[x][y]);
						int log2 = LogNumber.LogReturn(value[x][y+1]);
						int logDiff = Math.abs(log1-log2);
						if(logDiff == 0) total +=2;
						else {
							double score = 1.0/logDiff;
							total+=score;
						}
					}
				}
			}
		}
			
		return total;
	}
}
