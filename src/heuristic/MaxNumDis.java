package heuristic;

import game2048.Tile;

public class MaxNumDis implements Heuristic{

	public double function(Tile[] tile) {
		int max = 0;
		double p = 0.0;
		int i = 0;
		int j = 0;
		int n = tile.length;
		int imax = 0;
		int jmax = 0;
		for(i = 0; i<4;i++ ) {
			for(j = 1; j<4;j++) {
				max = tile[0].getValue();
				if(max < tile[i*4+j].getValue()) {
					max = tile[i].getValue();
					imax = i;
					jmax = j;
				}
			}
		}
		int a = 0;
		int b = 0;
		while( a<4) {
			while(b<4) {
				if(Math.abs(a-imax)+Math.abs(b-jmax)==0) {
					p = 2;
					break;
				}
				else if(Math.abs(a-imax)+Math.abs(b-jmax)==1){
					p = 1;
					break;
				}
				else{
					p = 0;
					break;
				}
				//b = b+3;
			}
			a=a+3;
		}
		return p;
	}
}