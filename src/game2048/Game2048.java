package game2048;
/*
 * Copyright 1998-2014 Konstantin Bulenkov http://bulenkov.com/about
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Konstantin Bulenkov
 */
public class Game2048 extends JPanel {
	private static final Color BG_COLOR = new Color(0xbbada0);
	private static final String FONT_NAME = "Arial";
	private static final int TILE_SIZE = 64;
	private static final int TILES_MARGIN = 16;

	private Tile[] myTiles;
	boolean myWin = false;
	boolean myLose = false;
	int myScore = 0;

	public Game2048() {
		setPreferredSize(new Dimension(340, 400));
		setFocusable(true);
		resetGame();
	}

	public void SetKeyAdapter(KeyAdapter keyAdapter) {
		addKeyListener(keyAdapter);
	}

	// Broke this out of the Game2048 constructor in case you want to write your
	// own.
	// Look at SetKeyAdapter()
	public KeyAdapter Game2048ClassicKeyAdapter() {
		return new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					resetGame();
				}
				if (!canMove()) {
					myLose = true;
				}

				if (!myWin && !myLose) {
					switch (e.getKeyCode()) {
					case KeyEvent.VK_LEFT:
						left();
						break;
					case KeyEvent.VK_RIGHT:
						right();
						break;
					case KeyEvent.VK_DOWN:
						down();
						break;
					case KeyEvent.VK_UP:
						up();
						break;
					}
				}

				if (!myWin && !canMove()) {
					myLose = true;
				}

				repaint();
			}
		};
	}

	public void resetGame() {
		myScore = 0;
		myWin = false;
		myLose = false;
		myTiles = new Tile[4 * 4];
		for (int i = 0; i < myTiles.length; i++) {
			myTiles[i] = new Tile();
		}
		addTile();
		addTile();
	}

	public void left() {
		boolean needAddTile = false;
		for (int i = 0; i < 4; i++) {
			Tile[] line = getLine(i);
			Tile[] merged = mergeLine(moveLine(line));
			setLine(i, merged);
			if (!needAddTile && !compare(line, merged)) {
				needAddTile = true;
			}
		}

		if (needAddTile) {
			addTile();
		}
	}

	public void right() {
		myTiles = rotate(180);
		left();
		myTiles = rotate(180);
	}

	public void up() {
		myTiles = rotate(270);
		left();
		myTiles = rotate(90);
	}

	public void down() {
		myTiles = rotate(90);
		left();
		myTiles = rotate(270);
	}

	private Tile tileAt(int x, int y) {
		return myTiles[x + y * 4];
	}

	private void addTile() {
		List<Tile> list = availableSpace();
		if (!availableSpace().isEmpty()) {
			int index = (int) (Math.random() * list.size()) % list.size();
			Tile emptyTime = list.get(index);
			int newNum = Math.random() < 0.9 ? 2 : 4;
			emptyTime.setValue(newNum);
		}
	}

	private List<Tile> availableSpace() {
		final List<Tile> list = new ArrayList<Tile>(16);
		for (Tile t : myTiles) {
			if (t.isEmpty()) {
				list.add(t);
			}
		}
		return list;
	}

	private boolean isFull() {
		return availableSpace().size() == 0;
	}

	boolean canMove() {
		if (!isFull()) {
			return true;
		}
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				Tile t = tileAt(x, y);
				if ((x < 3 && t.getValue().intValue() == tileAt(x + 1, y).getValue().intValue())
						|| ((y < 3) && t.getValue().intValue() == tileAt(x, y + 1).getValue().intValue())) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean compare(Tile[] line1, Tile[] line2) {
		if (line1 == line2) {
			return true;
		} else if (line1.length != line2.length) {
			return false;
		}

		for (int i = 0; i < line1.length; i++) {
			if (line1[i].getValue().intValue() != line2[i].getValue().intValue()) {
				return false;
			}
		}
		return true;
	}

	private Tile[] rotate(int angle) {
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
				newTiles[(newX) + (newY) * 4] = tileAt(x, y);
			}
		}
		return newTiles;
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

	private Tile[] mergeLine(Tile[] oldLine) {
		LinkedList<Tile> list = new LinkedList<Tile>();
		for (int i = 0; i < 4 && !oldLine[i].isEmpty(); i++) {
			int num = oldLine[i].getValue();
			if (i < 3 && oldLine[i].getValue().intValue() == oldLine[i + 1].getValue().intValue()) {
				num *= 2;
				myScore += num;
				// int ourTarget = 2048;
				// if (num == ourTarget) {
				// myWin = true;
				// }
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

	private Tile[] getLine(int index) {
		Tile[] result = new Tile[4];
		for (int i = 0; i < 4; i++) {
			result[i] = tileAt(i, index);
		}
		return result;
	}

	private void setLine(int index, Tile[] re) {
		System.arraycopy(re, 0, myTiles, index * 4, 4);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(BG_COLOR);
		g.fillRect(0, 0, this.getSize().width, this.getSize().height);
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				drawTile(g, myTiles[x + y * 4], x, y);
			}
		}
	}

	private void drawTile(Graphics g2, Tile tile, int x, int y) {
		Graphics2D g = ((Graphics2D) g2);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
		int value = tile.getValue();
		int xOffset = offsetCoors(x);
		int yOffset = offsetCoors(y);
		g.setColor(tile.getBackground());
		g.fillRoundRect(xOffset, yOffset, TILE_SIZE, TILE_SIZE, 14, 14);
		g.setColor(tile.getForeground());
		final int size = value < 100 ? 36 : value < 1000 ? 32 : 24;
		final Font font = new Font(FONT_NAME, Font.BOLD, size);
		g.setFont(font);

		String s = String.valueOf(value);
		final FontMetrics fm = getFontMetrics(font);

		final int w = fm.stringWidth(s);
		final int h = -(int) fm.getLineMetrics(s, g).getBaselineOffsets()[2];

		if (value != 0)
			g.drawString(s, xOffset + (TILE_SIZE - w) / 2, yOffset + TILE_SIZE - (TILE_SIZE - h) / 2 - 2);

		if (myWin || myLose) {
			g.setColor(new Color(255, 255, 255, 30));
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(new Color(78, 139, 202));
			g.setFont(new Font(FONT_NAME, Font.BOLD, 48));
			if (myWin) {
				g.drawString("You won!", 68, 150);
			}
			if (myLose) {
				g.drawString("Game over!", 50, 130);
				g.drawString("You lose!", 64, 200);
			}
			if (myWin || myLose) {
				g.setFont(new Font(FONT_NAME, Font.PLAIN, 16));
				g.setColor(new Color(128, 128, 128, 128));
				g.drawString("Press ESC to play again", 80, getHeight() - 40);
			}
		}
		g.setFont(new Font(FONT_NAME, Font.PLAIN, 18));
		g.drawString("Score: " + myScore, 200, 365);

	}

	private static int offsetCoors(int arg) {
		return arg * (TILES_MARGIN + TILE_SIZE) + TILES_MARGIN;
	}

	public Tile[] getBoard() {
		return myTiles;
	}

	public void refreshBoard() {
		repaint();
	}

	public static void main(String[] args) {
		JFrame game = new JFrame();
		game.setTitle("2048 Game");
		game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		game.setSize(340, 400);
		game.setResizable(false);

		Game2048 game2048 = new Game2048();
		// setting classic game
		game2048.SetKeyAdapter(game2048.Game2048ClassicKeyAdapter());
		game.add(game2048);

		game.setLocationRelativeTo(null);
		game.setVisible(true);
	}
}