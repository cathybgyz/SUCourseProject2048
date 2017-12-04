package heuristic;

import game2048.Tile;
import heuristic.LogNumber;

/*
 * Heuristic function to give the score relying on the value of the tile.
 * By calculating the result of the sum of logarithm divided by the number of tiles determine the amount of
 * tiles with small value. The biggest score is 12 and the smallest score is 1.
 *
 * Author: Tianyu Zhang
 */
public class SmallNumSum implements Heuristic {

	public double function(Tile[] tile) {
		int n = tile.length;
		double sum = 0.0;
		int count = 0;
		for (Tile t : tile) {
			if (t.getValue() == 0)
				count++;
			else
				sum = Math.pow(LogNumber.LogReturn(t.getValue()), 3) + sum;
		}
		
		sum = sum / (n - count);
		return sum;
	}
}