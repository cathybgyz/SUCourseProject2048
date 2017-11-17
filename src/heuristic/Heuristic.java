package heuristic;

import game2048.Tile;

public interface Heuristic {
	public double function(Tile tile[]);
}
