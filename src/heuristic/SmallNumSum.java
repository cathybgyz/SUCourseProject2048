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
public class SmallNumSum implements Heuristic{

	public double function(Tile[] tile) {
		int n = tile.length;
		double score = 0.0;
		double sum = 0.0;
		for(Tile t : tile) {
			sum = LogNumber.LogReturn(t.getValue())+sum;
		}
		int count = 0;
		for (Tile t : tile) {
			if (t.getValue() == 0)
				count++;
		}
		sum = sum/(n-count);
		if (sum < 2 ) {score = 1;}
		else if(sum>= 2 && sum <3) {score = 2;}
		else if(sum>= 3 && sum <4) {score = 3;}
		else if(sum>= 4 && sum <5) {score = 4;}
		else if(sum>= 5 && sum <6) {score = 5;}
		else if(sum>= 6 && sum <7) {score = 6;}
		else if(sum>= 7 && sum <8) {score = 7;}
		else if(sum>= 8 && sum <9) {score = 8;}
		else if(sum>= 9 && sum <10) {score = 9;}
		else if(sum>= 10 && sum <11) {score = 10;}
		else if(sum>= 11 && sum <12) {score = 11;}
		else  {score = 12;}
		return score;
	}
}