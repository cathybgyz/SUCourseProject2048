package heuristic;

import game2048.Tile;

/*
 * Heuristic function to give the score depending on the distance between the tile with maximum
 * number and the vertex
 * For the tile with the maximum number, if the distance to one of the vertex is 0, then it will
 * get score of 2. If the distance is 1 then the score will be 1 otherwise the score will be 0.
 *
 * Author: Tianyu Zhang
 */

public class MaxNumDis implements Heuristic{
	@Override
	public double function(Tile[] tile) {
		int array2d[][] = new int[4][4];
		int score = 0;
		int count = 0;
		int k = 0;
		int max = 0;
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
			   array2d[i][j] = tile[k].getValue();
			   k++;
		   }
		}
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
			   if(max < array2d[i][j])
				   max = array2d[i][j];
		   }
		}
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
			   if(array2d[i][j] == max)
				   count++;
		   }
		}
			int scorel[] = new int[count];
			int m = 0;
			for(int i = 0; i < 4; i++) {
				for(int j = 0; j < 4; j++) {
					if(array2d[i][j] == max) {
						
						if (Math.min(Math.min(i+j, i+3-j), Math.min(3-i+j, 6-i-j)) == 0)
							scorel[m] = 2;
						else if(Math.min(Math.min(i+j, i+3-j), Math.min(3-i+j, 6-i-j)) == 1)
							scorel[m] = 1;
						else
							scorel[m] = 0;
						m++;
						
					}

				}
			}
			for(int i= 0; i < count; i++) {
				if (score <= scorel[i])
					score = scorel[i];
			}
		return score;
	}
}