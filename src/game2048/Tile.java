package game2048;

import java.awt.Color;

public class Tile {
	private int row;
	private int col;
	private Integer value;

    public Tile() {
    	this.row = -1;
		this.col = -1;
		this.value = 0;
    }

    public Tile(Tile t) {
    	this.row = t.getRow();
    	this.col = t.getCol();
    	this.value = t.getValue();
    }
    
    public Tile(Integer num) {
      value = num;
    }
    
    /**
	 * tile constructor
	 * 
	 * @param r The row index
	 * @param c The column index
	 * @param v The tile value
	 */
	public Tile(int r, int c, Integer v) {
		this.row = r;
		this.col = c;
		this.value = v;
	}

    public boolean isEmpty() {
      return value == 0;
    }

    public Color getForeground() {
      return value < 16 ? new Color(0x776e65) :  new Color(0xf9f6f2);
    }
    

    public Color getBackground() {
      switch (value) {
        case 2:    return new Color(0xeee4da);
        case 4:    return new Color(0xede0c8);
        case 8:    return new Color(0xf2b179);
        case 16:   return new Color(0xf59563);
        case 32:   return new Color(0xf67c5f);
        case 64:   return new Color(0xf65e3b);
        case 128:  return new Color(0xedcf72);
        case 256:  return new Color(0xedcc61);
        case 512:  return new Color(0xedc850);
        case 1024: return new Color(0xedc53f);
        case 2048: return new Color(0xedc22e);
      }
      return new Color(0xcdc1b4);
    }
    
    public int getRow() {
		return this.row;
	}

	public int getCol() {
		return this.col;
	}

	public Integer getValue() {
		return this.value;
	}
	
	public void setCol(int c) {
		this.col = c;
	}
	
	public void setRow(int r) {
		this.row = r;
	}
	
	public void setValue(Integer v) {
		this.value = v;
	}
}
