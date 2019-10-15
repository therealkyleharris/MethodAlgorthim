package UnitFinder;

import java.util.HashSet;

public class Unit {
	private HashSet<Node> nodes;
	private Node root;
	
	public Unit(HashSet<Node> nodes, Node root) {
		this.nodes = nodes;
		this.root = root;
	}
	
	public HashSet<Node> getNodes(){
		return nodes;
	}
	
	public Node getRoot(){
		return root;
	}

	public boolean areNodesIdentical(Unit unit) {
		return this.nodes.equals(unit.getNodes());
	}
}
