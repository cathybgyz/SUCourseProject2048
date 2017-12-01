package game2048;

import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.awt.Robot;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import heuristic.Heuristic;
import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;

public class AI {
	public static Game2048 game2048; // get your game data here!
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
	// TODO: figure out how to play the game in this method, game2048 has all ur data
	//
	public static void RunAi(Game2048 game) {
		int secondsPassed = 0;
		
		
		try {
			while(true) {
				
				// Try move and get score
				
				
				
				// then make your move with this method call!
				MakeMove(Move.Left);
				MakeMove(Move.Right);
		
				// check time, since we are using Robot we need to kill it if we fail.
				//another way to do this is create a "watchdog" thread that needs to be
				//checked every x time.
				Thread.sleep(1000);
				secondsPassed++;
				if (secondsPassed >= MaxTimeSec)
					System.exit(0);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(0);
		}
	}
	
	public Move greedy(Game2048 game) {
		// Get 4 direction move
		Tile[] nextBoard = new Tile[16];
		
	}
  
	public static enum Move {
		Up,
		Down,
		Left,
		Right
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


	private Tile[] predictLeft(Tile[] origin) {
		Tile[] predicted = new Tile[16];
		for (int i = 0; i < 4; i++) {
		      Tile[] line = getLine(i,origin);
		      Tile[] merged = mergeLine(moveLine(line));
		      setLine(i, predicted,merged);
		}
		return predicted;
	}
	
	private Tile[] predictRight(Tile[] origin) {
		origin = rotate(180);
		Tile[] predicted = predictLeft(origin);
	    origin = rotate(180);
	    return predicted;
	}
	
	private Tile[] predictUp(Tile[] origin) {
		origin = rotate(270);
		Tile[] predicted = predictLeft(origin);
	    origin = rotate(90);
	    return predicted;
	}
	
	private Tile[] predictDown(Tile[] origin) {
		origin = rotate(90);
		Tile[] predicted = predictLeft(origin);
	    origin = rotate(270);
	    return predicted;
	}
	
	private  Tile[] getLine(int index,Tile[] origin) {
		Tile[] result = new Tile[4];
		for (int i = 0; i < 4; i++) {
			result[i] = origin[index*4+i];
		}
		return result;
	}
	
	  private Tile[] mergeLine(Tile[] oldLine) {
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
	  private void ensureSize(java.util.List<Tile> l, int s) {
		    while (l.size() != s) {
		      l.add(new Tile());
		    }
		  }
	  
	  private void setLine(int index, Tile[] origin,Tile[] re) {
		    System.arraycopy(re, 0, origin, index * 4, 4);
		  }
	  
	  private Tile[] moveLine(Tile[] oldLine) {
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
	  
	  private Tile[] rotate(int angle) {
		    Tile[] newTiles = new Tile[4 * 4];
		    int offsetX = 3, offsetY = 3;
		    if (angle == 90) {
		      offsetY = 0;
		    } else if (angle == 270) {
		      offsetX = 0;
		    }
}