package game2048;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import heuristic.AverageNum;
import heuristic.GeometricSequence;
import heuristic.MaxNumDis;
import heuristic.SameNumberDistance;
import heuristic.SmallNumSum;
import heuristic.SpaceNumber;
import heuristic.SquareArea;

public class AI {
	public static Game2048 game2048; // get your game data here!

	public static final boolean DEBUG = true;

	public static void main(String[] args) {
		JFrame game = new JFrame();
		game.setTitle("2048 Game");
		game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		game.setSize(340, 400);
		game.setResizable(false);

		game2048 = new Game2048();
		// setting classic game
		game2048.SetKeyAdapter(game2048.Game2048ClassicKeyAdapter());
		game.add(game2048);

		game.setLocationRelativeTo(null);
		game.setVisible(true);

		RunAi(game2048);
	}

	public static final int MaxTimeSec = 60;

	//
	// TODO: figure out how to play the game in this method, game2048 has all ur
	// data
	//
	public static void RunAi(Game2048 game) {
		int secondsPassed = 0;

		try {
			while (true) {

				if (DEBUG)
					System.out.println("==================New Move====================");
				// Try move and get score
				// then make your move with this method call!
				MakeMove(greedy(game));

				// check time, since we are using Robot we need to kill it if we fail.
				// another way to do this is create a "watchdog" thread that needs to be
				// checked every x time.
				// Thread.sleep(1000);
				// secondsPassed++;
				// if (secondsPassed >= MaxTimeSec)
				// System.exit(0);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(0);
		}
	}

	public static Move greedy(Game2048 game) {
		// Get 4 direction move
		Tile[] nextBoard = new Tile[16];
		double leftScore = 0;
		double rightScore = 0;
		double upScore = 0;
		double downScore = 0;
		double maxScore = 0;

		if(DEBUG) {
			System.out.println("Current board");
			printTiles(game.getBoard());
		}
		
		nextBoard = predictLeft(game.getBoard());
		
		if(DEBUG) {
			System.out.println("If go left");
			printTiles(nextBoard);
		}
		if (checkNoChange(nextBoard, game.getBoard())) {
			leftScore = getScore(nextBoard);
			if (maxScore < leftScore) {
				maxScore = leftScore;
			}
		}

		nextBoard = predictRight(game.getBoard());
		
		if(DEBUG) {
			System.out.println("If go right");
			printTiles(nextBoard);
		}
		
		if (checkNoChange(nextBoard, game.getBoard())) {
			rightScore = getScore(nextBoard);
			if (maxScore < rightScore)
				maxScore = rightScore;
		}

		nextBoard = predictUp(game.getBoard());
		
		if(DEBUG) {
			System.out.println("If go up");
			printTiles(nextBoard);
		}
		
		if (checkNoChange(nextBoard, game.getBoard())) {
			upScore = getScore(nextBoard);
			if (maxScore < upScore)
				maxScore = upScore;
		}

		nextBoard = predictDown(game.getBoard());
		
		if(DEBUG) {
			System.out.println("If go down");
			printTiles(nextBoard);
		}
		
		if (checkNoChange(nextBoard, game.getBoard())) {
			downScore = getScore(nextBoard);
			if (maxScore < downScore)
				maxScore = downScore;
		}

		if (maxScore == leftScore)
			return Move.Left;
		if (maxScore == rightScore) {
			return Move.Right;
		}
		if (maxScore == upScore)
			return Move.Up;
		if (maxScore == downScore)
			return Move.Down;

		return null;
	}

	public static enum Move {
		Up, Down, Left, Right
	}

	public static Boolean MakeMove(Move move) {
		try {
			Robot r = new Robot();

			int keyCode = 0;
			if (move == Move.Up)
				keyCode = KeyEvent.VK_UP;
			else if (move == Move.Down)
				keyCode = KeyEvent.VK_DOWN;
			else if (move == Move.Left)
				keyCode = KeyEvent.VK_LEFT;
			else if (move == Move.Right)
				keyCode = KeyEvent.VK_RIGHT;
			else
				return false;

			r.keyPress(keyCode);
			Thread.sleep(5); // 5 ms delay seems realistic, needed?
			r.keyRelease(keyCode);

			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(0);
		}
		return false;
	}

	public static boolean checkNoChange(Tile[] oldB, Tile[] newB) {
		for (int i = 0; i < 16; i++) {
			if (oldB[i] == null && newB[i] == null)
				continue;
			else if (oldB[i] == null)
				return true;
			else if (newB[i] == null)
				return true;
			else if (oldB[i].getValue() != newB[i].getValue())
				return true;
		}
		return false;
	}

	private static Tile[] predictLeft(Tile[] origin) {
		Tile[] predicted = new Tile[16];
		for (int i = 0; i < 4; i++) {
			Tile[] line = getLine(i, origin);
			Tile[] merged = mergeLine(moveLine(line));
			setLine(i, predicted, merged);
		}
		return predicted;
	}

	private static Tile[] predictRight(Tile[] origin) {
		origin = rotate(origin, 180);
		Tile[] predicted = predictLeft(origin);
		origin = rotate(origin, 180);
		predicted = rotate(predicted, 180);
		return predicted;
	}

	private static Tile[] predictUp(Tile[] origin) {
		origin = rotate(origin, 270);
		Tile[] predicted = predictLeft(origin);
		origin = rotate(origin, 90);
		predicted = rotate(predicted, 90);
		return predicted;
	}

	private static Tile[] predictDown(Tile[] origin) {
		origin = rotate(origin, 90);
		Tile[] predicted = predictLeft(origin);
		origin = rotate(origin, 270);
		predicted = rotate(predicted, 270);
		return predicted;
	}

	private static Tile[] getLine(int index, Tile[] origin) {
		Tile[] result = new Tile[4];
		for (int i = 0; i < 4; i++) {
			result[i] = origin[index * 4 + i];
		}
		return result;
	}

	private static Tile[] mergeLine(Tile[] oldLine) {
		LinkedList<Tile> list = new LinkedList<Tile>();
		for (int i = 0; i < 4 && !oldLine[i].isEmpty(); i++) {
			int num = oldLine[i].getValue();
			if (i < 3 && oldLine[i].getValue() == oldLine[i + 1].getValue()) {
				num *= 2;
				i++;
			}
			list.add(new Tile(num));
		}
		if (list.size() == 0) {
			return oldLine;
		} else {
			ensureSize(list, 4);
			return list.toArray(new Tile[4]);
		}
	}

	private static void ensureSize(java.util.List<Tile> l, int s) {
		while (l.size() != s) {
			l.add(new Tile());
		}
	}

	private static void setLine(int index, Tile[] origin, Tile[] re) {
		System.arraycopy(re, 0, origin, index * 4, 4);
	}

	private static Tile[] moveLine(Tile[] oldLine) {
		LinkedList<Tile> l = new LinkedList<Tile>();
		for (int i = 0; i < 4; i++) {
			if (!oldLine[i].isEmpty())
				l.addLast(oldLine[i]);
		}
		if (l.size() == 0) {
			return oldLine;
		} else {
			Tile[] newLine = new Tile[4];
			ensureSize(l, 4);
			for (int i = 0; i < 4; i++) {
				newLine[i] = l.removeFirst();
			}
			return newLine;
		}
	}

	private static Tile[] rotate(Tile[] target, int angle) {
		Tile[] newTiles = new Tile[4 * 4];
		int offsetX = 3, offsetY = 3;
		if (angle == 90) {
			offsetY = 0;
		} else if (angle == 270) {
			offsetX = 0;
		}
		double rad = Math.toRadians(angle);
		int cos = (int) Math.cos(rad);
		int sin = (int) Math.sin(rad);
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				int newX = (x * cos) - (y * sin) + offsetX;
				int newY = (x * sin) + (y * cos) + offsetY;
				newTiles[(newX) + (newY) * 4] = target[x + y*4];
			}
		}
		return newTiles;
	}

	private static double getScore(Tile[] tiles) {
		double score = 0;

		score += 1.71 * (new AverageNum()).function(tiles);
		score += 3.75 * (new GeometricSequence()).function(tiles);
		score += 15 * (new MaxNumDis()).function(tiles);
		score += 2.5 * (new SameNumberDistance()).function(tiles);
		score += 2 * (new SmallNumSum()).function(tiles);
		score += 3.33 * (new SpaceNumber()).function(tiles);
		score += 0.5 * (new SquareArea()).function(tiles);

		return score;
	}

	private static void printTiles(Tile[] t) {
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				System.out.print(t[x*4+y].getValue() + "\t");
			}
			System.out.println();
		}
	}
}
