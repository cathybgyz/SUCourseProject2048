package heuristic;

import game2048.Tile;

/**
 * Heuristic function to calculate the space number and give a score
 * 16 space = 16, 0 space = 0 
 * Author: Zimeng Lyu
 * **/
public class SpaceNumber implements Heuristic {
	@Override
	public double function(Tile[] tile) {
		int count = 0;
		for (Tile t : tile) {
			if (t.getValue() == 0)
				count++;
		}
		return (double)count;
	}
}
