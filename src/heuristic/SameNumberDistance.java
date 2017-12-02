package heuristic;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import game2048.Tile;

public class SameNumberDistance implements Heuristic {
	/**
	 * This is strategy evaluation function to estimate the score of current game
	 * board state by checking the distance of same numbers
	 * 
	 * @author Qinzhe Zhang
	 * @param board
	 *            Current game board state
	 * @return score evaluated
	 * @throws Exception
	 */
	@Override
	public double function(Tile board[]) {
		if (board.length != 16)
			return 0;

		double score = 0;
		Queue<Tile> tiles = new LinkedList<>(Arrays.asList(board));
		Set<Integer> existedValue = new HashSet<>();

		for (int x=0;x<4;x++){
			for(int y=0;y<4;y++) {
				board[x*4+y].setCol(x);
				board[x*4+y].setRow(y);
				existedValue.add(board[x*4+y].getValue());
			}
		}
		// Count distance of all pairs with the same value
		for (Integer curValue : existedValue) {
			// List of tiles with all the same value
			List<Tile> sameValueTiles = new LinkedList<>();
			int time = tiles.size();
			while (time > 0 && tiles.size() > 0) {
				Tile t = tiles.poll();
				if (t.getValue() == curValue) {
					sameValueTiles.add(t);
				} else {
					tiles.add(t);
				}
				time--;
			}

			// Continue if the value is 0
			if (curValue == 0)
				continue;

			// Check tile one by one to see the shortest distance of a specific value
			while (sameValueTiles.size() > 1) {
				Tile t = sameValueTiles.remove(0);
				int min = 100;
				for (Tile cur : sameValueTiles) {
					int distance = Math.abs(cur.getCol() - t.getCol()) + Math.abs(cur.getRow() - t.getRow());
					if (distance > 0 && distance < min)
						min = distance;
				}
				score += (6 - min)*curValue/Math.log(curValue)*Math.log(2);
			}
		}

		return score/3;
	}
}
