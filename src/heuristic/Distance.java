package heuristic;

import static org.junit.jupiter.api.Assumptions.assumingThat;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.border.TitledBorder;

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
			int distance = 6;
			for (Tile t : tile) {
				if (t.getValue() != cur)
					continue;
				int tmp = t.getCol() + t.getRow() - 3;
				if (tmp < distance)
					distance = tmp;
			}

			score += cur * Math.pow(0.1, distance);
		}
		return score/existedValue.size();
	}

}
