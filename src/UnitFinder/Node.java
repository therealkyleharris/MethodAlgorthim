package UnitFinder;

import java.util.ArrayList;

public class Node {

	public String name, id;
	public ArrayList<Node> children = new ArrayList<Node>();
	public ArrayList<Node> parents = new ArrayList<Node>();
	
	public Node(String name, String id) {
		this.name = name;
		this.id = id;
	}
	
	@Override
	public String toString() {
		return id + " - P:" + parents.size() + ", C:" + children.size() + " - " + name;
	}


	public String getName(){
		return this.name;
	}

	public String getId(){
		return this.id;
	}

	public ArrayList<Node> getParents(){
		return this.parents;
	}

	public ArrayList<Node> getChildren(){
		return this.children;
	}
}