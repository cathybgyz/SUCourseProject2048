package game2048;

import java.util.LinkedList;
import java.util.List;

import game2048.AI.Move;

public class Node {
	private Tile[] state;
	private int depth;
	private boolean isMaxPlayer;
	private double score;
	private List<Node> children;
    private Move direction;
    private double point;
    // setters and getters
    
    public Node(Tile[] state,int depth,boolean isMaxPlayer) throws Exception {
    	this.children = new LinkedList<Node>();
    	this.state = state;
    	this.isMaxPlayer = isMaxPlayer;
    	this.depth = depth;
    	this.score = AI.getScore(state);
    	this.point = -1;
    }
    
    public Node(Tile[] state,int depth,Move direction,boolean isMaxPlayer) throws Exception {
    	this.children = new LinkedList<Node>();
    	this.state = state;
    	this.isMaxPlayer = isMaxPlayer;
    	this.depth = depth;
    	this.direction = direction;
    	this.score = AI.getScore(state);
    	this.point = -1;
    }
    
    public Tile[] getState() {
    	return this.state;
    }
    
    public boolean isMax() {
    	return this.isMaxPlayer;
    }
    
    public int getDepth() {
    	return this.depth;
    }
    
    public void addChild(Node child) {
    	this.children.add(child);
    }
    
    public boolean hasChild() {
    	return (!this.children.isEmpty());
    }
    
    public List<Node> getChild(){
    	return this.children;
    }
    
    public void setPoint(double point) {
    	this.point = point;
    }
    
    public double getPoint() {
    	return this.point;
    }
    
    public double getScore() {
    	return this.score;
    }
    
    public Move getDirection() {
    	return this.direction;
    }
}
