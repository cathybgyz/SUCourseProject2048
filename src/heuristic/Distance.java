package heuristic;

import java.util.HashSet;
import java.util.Set;

import game2048.Tile;

public class Distance implements Heuristic {

	@Override
	public double function(Tile[] tile) {

		Set<Integer> existedValue = new HashSet<>();

		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				tile[x * 4 + y].setCol(x);
				tile[x * 4 + y].setRow(y);
				existedValue.add(tile[x * 4 + y].getValue());
			}
		}

		double score = 0;
		for (Integer cur : existedValue) {
			if(cur.intValue()==0)
				continue;
			int distance = 6;
			for (Tile t : tile) {
				if (t.getValue().intValue() != cur.intValue())
					continue;
				int tmp = t.getCol() + t.getRow() - 3;
				if (tmp < distance)
					distance = tmp;
			}

			score += LogNumber.LogReturn(cur) * Math.pow(0.1, distance);
		}
		return score;
	}

}
