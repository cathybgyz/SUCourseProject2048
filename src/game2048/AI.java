package game2048;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import heuristic.*;

public class AI {
	public static Game2048 game2048; // get your game data here!

	public static final boolean DEBUG = true;

	public static final boolean DEBUG_DETAILS = true;

	public static FileWriter fw;

	public static void main(String[] args) throws Exception {
		fw = new FileWriter("test.txt");
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
		fw.close();
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

				if (DEBUG) {
					//System.out.println("==================New Move====================");
					fw.append("==================New Move====================\n");
				}
				// Try move and get score
				// then make your move with this method call!
				//MakeMove(greedy(game));
				MakeMove(MiniMaxAI(game));
				// check time, since we are using Robot we need to kill it if we fail.
				// another way to do this is create a "watchdog" thread that needs to be
				// checked every x time.
				Thread.sleep(50);
				// secondsPassed++;
				// if (secondsPassed >= MaxTimeSec)
				// System.exit(0);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(0);
		}
	}

	public static Move greedy(Game2048 game) throws Exception {
		// Get 4 direction move
		Tile[] nextBoard = new Tile[16];
		double leftScore = 0;
		double rightScore = 0;
		double upScore = 0;
		double downScore = 0;
		double maxScore = 0;
		double offset = 0;

		if (DEBUG) {
			System.out.println("Current board");
			fw.append("Current board\n");
			printTiles(game.getBoard());
		}

		nextBoard = predictLeft(game.getBoard());

		if (DEBUG_DETAILS) {
			System.out.println("If go left");
			fw.append("If go left\n");
			printTiles(nextBoard);
		}
		if (checkNoChange(nextBoard, game.getBoard())) {
			leftScore = getScore(nextBoard) + offset;
			if (maxScore < leftScore) {
				maxScore = leftScore;
			}
		}

		nextBoard = predictRight(game.getBoard());

		if (DEBUG_DETAILS) {
			System.out.println("If go right");
			fw.append("If go right\n");
			printTiles(nextBoard);
		}

		if (checkNoChange(nextBoard, game.getBoard())) {
			rightScore = getScore(nextBoard);
			if (maxScore < rightScore)
				maxScore = rightScore;
		}

		nextBoard = predictUp(game.getBoard());

		if (DEBUG_DETAILS) {
			System.out.println("If go up");
			fw.append("If go up\n");
			printTiles(nextBoard);
		}

		if (checkNoChange(nextBoard, game.getBoard())) {
			upScore = getScore(nextBoard);
			if (maxScore < upScore)
				maxScore = upScore;
		}

		nextBoard = predictDown(game.getBoard());

		if (DEBUG_DETAILS) {
			System.out.println("If go down");
			fw.append("If go down\n");
			printTiles(nextBoard);
		}

		if (checkNoChange(nextBoard, game.getBoard())) {
			downScore = getScore(nextBoard) + offset;
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

		if (DEBUG) {
			System.out.println("Get next move error");
		}

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
			else if (oldB[i].getValue().intValue() != newB[i].getValue().intValue())
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
			if (i < 3 && oldLine[i].getValue().intValue() == oldLine[i + 1].getValue().intValue()) {
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
				newTiles[(newX) + (newY) * 4] = target[x + y * 4];
			}
		}
		return newTiles;
	}

	public static double getScore(Tile[] tiles) throws Exception {
		double score = 0;
		double tmp = 0;
		double tmp2 = 0;
		double Weight_BePair = 50;
		double Weight_Distance = 0.1;
		double Weight_Smoothness = 0.7;
		double Weight_GeometricSequence = 1.75;
		double Weight_MaxNumDis = 30;
		double Weight_SameNumberDistance = 60;
		double Weight_SmallNumSum = 8;
		double Weight_SpaceNumber = 70;
		double Weight_SquareArea = 4;

		// ----------
		if (DEBUG) {
			tmp = (new BePair()).function(tiles);
			//System.out.println("BePair:" + tmp);
			tmp2 = Weight_BePair * tmp;
			fw.append("BePair:" + Weight_BePair + "*" + tmp + ": " + tmp2 + "\n");
		}
		score += tmp2;
		// ------------------

		// ----------
		if (DEBUG) {
			tmp = (new Distance()).function(tiles);
			//System.out.println("Distance:" + tmp);
			tmp2 = Weight_Distance * tmp;
			fw.append("Distance:" + Weight_Distance + "*" + tmp + ": " + tmp2 + "\n");
		}
		score += tmp2;
		// ------------------

		if (DEBUG) {
			tmp = (new Smoothness()).function(tiles);
			//System.out.println("Smoothness:" + tmp);
			tmp2 = Weight_Smoothness * tmp;
			fw.append("Smoothness:" + Weight_Smoothness + "*" + tmp + ": " + tmp2 + "\n");
		}
		score += tmp2;

		if (DEBUG) {
			tmp = (new GeometricSequence()).function(tiles);
			//System.out.println("GeometricSequence:" + tmp);
			tmp2 = Weight_GeometricSequence * tmp;
			fw.append("GeometricSequence:" + Weight_GeometricSequence + "*" + tmp + ": " + tmp2 + "\n");

		}

		score += tmp2;

		if (DEBUG) {
			tmp = (new MaxNumDis()).function(tiles);
			//System.out.println("MaxNumDis:" + tmp);
			tmp2 = Weight_MaxNumDis * tmp;
			fw.append("MaxNumDis:" + Weight_MaxNumDis + "*" + tmp + ": " + tmp2 + "\n");

		}
		score += tmp2;

		if (DEBUG) {
			tmp = (new SameNumberDistance()).function(tiles);
			//System.out.println("SameNumberDistance:" + tmp);
			tmp2 = Weight_SameNumberDistance * tmp;
			fw.append("SameNumberDistance:" + Weight_SameNumberDistance + "*" + tmp + ": " + tmp2 + "\n");

		}
		score += tmp2;

		if (DEBUG) {
			tmp = (new SmallNumSum()).function(tiles);
			//System.out.println("SmallNumSum:" + tmp);
			tmp2 = Weight_SmallNumSum * tmp;
			fw.append("SmallNumSum:" + Weight_SmallNumSum + "*" + tmp + ": " + tmp2 + "\n");

		}
		score += tmp2;

		if (DEBUG) {
			tmp = (new SpaceNumber()).function(tiles);
			//System.out.println("SpaceNumber:" + tmp);
			tmp2 = Weight_SpaceNumber * tmp;
			fw.append("SpaceNumber:" + Weight_SpaceNumber + "*" + tmp + ": " + tmp2 + "\n");

		}
		score += tmp2;

		if (DEBUG) {
			tmp = (new SquareArea()).function(tiles);
			//System.out.println("SquareArea:" + tmp);
			tmp2 = Weight_SquareArea * tmp;
			fw.append("SquareArea:" + Weight_SquareArea + "*" + tmp + ": " + tmp2 + "\n");

		}
		score += tmp2;

		if (DEBUG) {
			//System.out.println("Score:" + score);
			fw.append("Score:" + score + "\n");
		}
		return score;
	}

	private static void printTiles(Tile[] t) throws Exception {
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				System.out.print(t[x * 4 + y].getValue() + "\t");
				fw.append(t[x * 4 + y].getValue() + "\t");
			}
			System.out.println();
			fw.append("\n");
		}
	}
	
	
	
	public static class child{
		Tile[] state;
		Move direction;
		
		public child(Tile[] state, Move direction) {
			this.state = state;
			this.direction = direction;
		}
	}
	
	//implementing minimax
	public static List<child> getPossibleList(Tile[] currentState) throws Exception{
		List<child> c = new LinkedList<child>();
		
		Tile[] nextBoard;
		nextBoard = predictLeft(currentState);
		if(checkNoChange(currentState,nextBoard)) {
			c.add(new child(nextBoard,Move.Left));
		}
		
		nextBoard = predictRight(currentState);
		if(checkNoChange(currentState,nextBoard)) {
			c.add(new child(nextBoard,Move.Right));
		}
		
		nextBoard = predictUp(currentState);
		if(checkNoChange(currentState,nextBoard)) {
			c.add(new child(nextBoard,Move.Up));
		}
		
		nextBoard = predictDown(currentState);
		if(checkNoChange(currentState,nextBoard)) {
			c.add(new child(nextBoard,Move.Down));
		}
		
		return c;
	}
	
	public static Move MiniMaxAI(Game2048 game) throws Exception {
		MinimaxMultiThreads m = new MinimaxMultiThreads();
		Tile[] currentState = game.getBoard();
		int MaxDepth = 4;
		m.initialize(currentState);
		Node root = m.tree.getRoot();

		//int n = root.getChild().size();
		//for(int i=0;i<n;i++) {
			//System.out.print(root.getChild().get(i)+"\t");
		//}
		//System.out.println();
		
		
		//m.constructTree(m.tree.getRoot(), MaxDepth);
		m.runConstruction(m.tree.getRoot(), MaxDepth);
				
		Node choice = m.getBestChild(m.tree.getRoot(), MaxDepth);
		
		
		
		
		
		if(choice != null) return choice.getDirection();
		else return Move.Left;
		

	}
}
