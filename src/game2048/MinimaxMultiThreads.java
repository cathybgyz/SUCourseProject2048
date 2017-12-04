package game2048;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import game2048.AI.Move;
import game2048.AI.child;

public class MinimaxMultiThreads {
	Tree tree;

	public void initialize(Tile[] currentState) throws Exception {
		tree = new Tree();
		Node root = new Node(currentState, 0, true);
		tree.setRoot(root);
	}

	public void runConstruction(Node parent, int depth) {
		//System.out.println("construct a level");
		constructTree c = new constructTree(parent, depth);
		c.start();
		try {
			c.t.join();
		} catch (InterruptedException e) {
			System.out.println("wait construction process error");
			e.printStackTrace();
		}
	}

	public Node getBestChild(Node parent, int MaxDepth) {
		List<Node> child = parent.getChild();

		while (child == null) {
			//System.out.println("Get bestchild waiting construction");
			child = parent.getChild();
		}

		int length = child.size();
		if (parent.getDepth() == MaxDepth - 1) {
			for (int i = 0; i < length; i++) {
				Node temp = child.get(i);
				double score = temp.getScore();
				temp.setPoint(score);
			}
		}
		Node best = null;
		boolean isMax = parent.isMax();
		double bestPoint = -1;
		for (int i = 0; i < length; i++) {
			Node temp = child.get(i);
			if (temp.getPoint() == -1) {
				Node t = getBestChild(temp, MaxDepth);
				if (t == null)
					continue;
				double point = t.getPoint();
				temp.setPoint(point);
			}
			if (i == 0) {
				bestPoint = temp.getPoint();
				best = temp;
			}
			if (isMax && bestPoint < temp.getPoint()) {
				bestPoint = temp.getPoint();
				best = temp;
			} else if (!isMax && bestPoint > temp.getPoint()) {
				bestPoint = temp.getPoint();
				best = temp;
			}
		}
		return best;
	}

	class constructTree implements Runnable {
		public Thread t;
		public Node parentNode;
		public int MaxDepth;

		constructTree(Node parent, int depth) {
			parentNode = parent;
			MaxDepth = depth;
		}

		private List<Tile> availableSpace(Tile[] state) {
			final List<Tile> list = new ArrayList<Tile>(16);
			for (Tile t : state) {
				if (t.isEmpty()) {
					list.add(t);
				}
			}
			return list;
		}

		private List<Tile[]> addTile(Tile[] state) {
			List<Tile> list = availableSpace(state);
			List<Tile[]> children = new LinkedList<Tile[]>();
			if (!list.isEmpty()) {
				while (!list.isEmpty()) {
					Tile emptyTime = list.remove(0);
					emptyTime.setValue(2);
					Tile[] temp1 = new Tile[16];
					for (int i = 0; i < 16; i++) {
						temp1[i] = new Tile(state[i]);
					}
					children.add(temp1);
					emptyTime.setValue(4);
					Tile[] temp2 = new Tile[16];
					for (int i = 0; i < 16; i++) {
						temp2[i] = new Tile(state[i]);
					}
					children.add(temp2);
					emptyTime.setValue(0);
				}
			} else {
				Tile[] temp = new Tile[16];
				for (int i = 0; i < 16; i++) {
					temp[i] = new Tile(state[i]);
				}
				children.add(temp);
			}
			return children;
		}

		public void construct() {
			boolean isChildMaxPlayer = !parentNode.isMax();
			int depth = parentNode.getDepth() + 1;
			if (isChildMaxPlayer) {
				List<Tile[]> childNode = addTile(parentNode.getState());
				while (!childNode.isEmpty()) {
					Tile[] state = childNode.remove(0);
					Node temp = null;

					try {
						temp = new Node(state, depth, isChildMaxPlayer);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (temp != null)
						parentNode.addChild(temp);
				}
			} else {
				List<child> childNode = null;
				try {
					childNode = AI.getPossibleList(parentNode.getState());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (childNode != null)
					while (!childNode.isEmpty()) {
						child c = childNode.remove(0);
						Tile[] state = c.state;
						Move m = c.direction;
						Node temp = null;
						try {
							temp = new Node(state, depth, m, isChildMaxPlayer);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						if (temp != null)
							parentNode.addChild(temp);
					}
			}
			if (parentNode.hasChild() && depth <= MaxDepth) {
				List<Node> children = parentNode.getChild();
				int length = children.size();
				constructTree[] subT = new constructTree[length];
				for (int i = 0; i < length; i++) {
					subT[i] = new constructTree(children.get(i), MaxDepth);
					subT[i].start();
				}
				
				for(int i = 0; i<length;i++)
					try {
						subT[i].t.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}

		}

		public void run() {
			//System.out.println("a new construction Thread.");
			construct();
			//System.out.println("Thread exiting.");
		}

		public void start() {
			if (t == null) {
				t = new Thread(this, "construct Child");
				t.start();
			}
		}
	}
}
