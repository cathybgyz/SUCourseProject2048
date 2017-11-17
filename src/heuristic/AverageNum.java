package heuristic;

import game2048.Tile;

/**
 * Heuristic function to judge the average log number of all the tiles.
 * Consider that the current state with larger average log number is better.
 * Theoretically, the highest point should be 12, and the lowest point should be 1;
 * 
 * Author: Xiangde Zeng
 * **/
public class AverageNum implements Heuristic {
	
	public double function(Tile[] tile) {
		int n = tile.length;
		int count = 0;
		int sum = 0;
		for(int i = 0;i<n;i++) {
			if(tile[i].getValue() != 0) {
				count++;
				sum += LogNumber.LogReturn(tile[i].getValue());
			}
		}
		double point = (double)(sum)/count;
		return point;
	}
}
