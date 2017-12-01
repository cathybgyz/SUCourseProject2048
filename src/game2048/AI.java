package game2048;

import java.awt.event.KeyEvent;
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
}
