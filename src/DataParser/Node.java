package DataParser;

import java.util.ArrayList;

public class Node {
	String name, id; 
	ArrayList<Node> children = new ArrayList<Node>();
	ArrayList<Node> parents = new ArrayList<Node>();
	
	public Node(String name, String id) {
		this.name = name;
		this.id = id;
	}
	
	@Override
	public String toString() {
		return id + " - P:" + parents.size() + ", C:" + children.size() + " - " + name;
	}
}