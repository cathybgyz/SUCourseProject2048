package heuristic;

import game2048.Tile;

/**
 * Heuristic function to judge the biggest square area existed. 
 * If this area is smaller, the pointer whould be greater.
 * 
 * Author: Xiangde Zeng
 * **/

public class SquareArea implements Heuristic{
	public double function(Tile[] tile) {
		int n = tile.length;
		int l=0;
		int w=0;
		int lcount=0;
		int[] wcount = new int[4];
		for(int i:wcount)i=0;
		
		for(int i=0;i<n;i++) {
			if(tile[i].getValue() != 0) {
				lcount++;
				wcount[i%4]++;
			}
			if(i%4 == 3) {
				l = Math.max(lcount, l);
				lcount = 0;
			}
		}
		w = wcount[0];
		for(int i:wcount) {
			if(w>i) w = i;
		}
		int area = w * l;
		return (double)(16-area);
	}
}
