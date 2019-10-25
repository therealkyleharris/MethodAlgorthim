package UnitFinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Node {

	public String name, id, module;
	public ArrayList<Node> children = new ArrayList<Node>();
	public ArrayList<Node> parents = new ArrayList<Node>();
	
	public Node(String name, String id, String module) {
		this.name = name;
		this.id = id;
		this.module = module;
	}
	
	@Override
	public String toString() {
		return id + " - M:" + module + " - P:" + parents.size() + ", C:" + children.size() + " - " + name;
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
	
	public Collection<Node> getParentsInTheSameModule(){
		return getNodesOfGivenModule(parents);
	}
	
	public Collection<Node> getChildrenInTheSameModule(){
		return getNodesOfGivenModule(children);
	}
	
	private Collection<Node> getNodesOfGivenModule(Collection<Node> nodes){
		Predicate<Node> nodeFilter = item -> item.module.equals(module);
		return nodes.stream().filter(nodeFilter).collect(Collectors.toList());
	}

}