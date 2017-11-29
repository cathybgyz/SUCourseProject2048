package heuristic;

import game2048.Tile;

/**
 * Heuristic function to get the Geometric Sequence around a tile. 
 * For each tile, if a tile which next to it (upper one and right one)is 2 or 0.5 times of this tile, score + 1
 * Author: Zimeng Lyu
 * **/
public class GeometricSequence implements Heuristic {
	@Override
	public double function(Tile[] tile) {
		// reshape 1d tile array to 2d value array
		int array2d[][] = new int[4][4];
		int k = 0;
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
			   array2d[i][j] = tile[k].getValue();
			   k++;
		   }
		}
   
		// calculate score
		int score = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				// check right
				if (i < 3) {
					if (array2d[i][j]==0) continue;
					if (array2d[i+1][j]==0) continue;
					if (array2d[i][j] == array2d[i+1][j]*2)
						score++;
					else if (array2d[i][j] == array2d[i+1][j]/2)
						score++;
				}
				// check bottom
				if  (j < 3) {
					if (array2d[i][j]==0) continue;
					if (array2d[i][j+1]==0) continue;
					if (array2d[i][j] == array2d[i][j+1]*2)
						score++;
					else if (array2d[i][j] == array2d[i][j+1]/2)
						score++;
				}
			}
		}
	
		return (double)score;
	}

}
