package heuristic;

import game2048.Tile;

/**
 * Heuristic function to get the Geometric Sequence around a tile. 
 * V1.0
 * For each tile, if a tile which next to it (upper one and right one)is 2 or 0.5 times of this tile, score + 1
 * V1.1
 * For the largest number sequence, the score is 5
 * Author: Zimeng Lyu
 * **/
public class GeometricSequence implements Heuristic {
	@Override
	public double function(Tile[] tile) {
		// reshape 1d tile array to 2d value array
		int array2d[][] = new int[4][4];
		int k = 0;
		int max = 0;
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
			   array2d[i][j] = tile[k].getValue();
			   if(max < array2d[i][j])
				   max = array2d[i][j];
			   k++;
		   }
		}

		// calculate score
		int score = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				// check right
				if  (j < 3) {
					if (array2d[i][j]==0) continue;
					if (array2d[i][j+1]==0) continue;
					if (array2d[i][j] == array2d[i][j+1]*2)
						if (array2d[i][j]==max)
							score= score +10;
						else score++;
					else if (array2d[i][j] == array2d[i][j+1]/2)
						if (array2d[i][j+1]==max)
							score= score +5;
						else score++;
						
				}
				// check bottom
				if (i < 3) {
					if (array2d[i][j]==0) continue;
					if (array2d[i+1][j]==0) continue;
					if (array2d[i][j] == array2d[i+1][j]*2)
						if (array2d[i][j]==max)
							score= score +5;
						else score++;
					else if (array2d[i][j] == array2d[i+1][j]/2)
						if (array2d[i+1][j]==max)
							score= score +5;
						else score++;
						
				}
			}
		}
	
		return (double)score;
	}

}
