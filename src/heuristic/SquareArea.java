package heuristic;

import game2048.Tile;

/**
 * Heuristic function to judge the biggest square area existed. 
 * If this area is smaller, the pointer whould be greater.
 * 
 * Author: Xiangde Zeng
 * **/

public class SquareArea implements Heuristic{
	@Override
	public double function(Tile[] tile) {
		int n = tile.length;
		int lStart=4;
		int lEnd = -1;
		int wStart = 4;
		int wEnd = -1;
		
		for(int i=0;i<n;i++) {
			if(tile[i].getCol()<lStart&&tile[i].getValue()!=0) lStart = tile[i].getCol();
			if(tile[i].getCol()>lEnd&&tile[i].getValue()!=0) lEnd = tile[i].getCol();
			if(tile[i].getRow()<wStart&&tile[i].getValue()!=0) wStart = tile[i].getRow();
			if(tile[i].getRow()>wEnd&&tile[i].getValue()!=0) wEnd = tile[i].getRow();
		}
		int w = wEnd-wStart + 1;
		int l = lEnd - lStart + 1;
		int area = w * l;
		return (double)(16-area);
	}
}
