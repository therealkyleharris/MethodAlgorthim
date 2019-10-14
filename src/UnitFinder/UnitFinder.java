package UnitFinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import DataParser.DataParser;
import DataParser.Node;

public class UnitFinder {
	public static void main(String[] args) {
		try {
			HashMap<String, Node> tree = DataParser.readFile("TimeCoreData.csv", 2, 1, 5);
			//String startingMethodID = "19$144724";	//get Down Traversal 7(SS)*S, 19$144724
			//String startingMethodID = "19$144756";	//Method Traversal@get Infinite Looping(SS)*S, 19$144756
			//String startingMethodID = "19$144751";	//get Down Traversal -2(SS), 19$144751
			String startingMethodID = "19$144732";		//get Down Traversal 3(SS)*S, 19$144732
			Node startingMethod = tree.get(startingMethodID);
			findUnit(startingMethod);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Unit Finder Done");
	}
	
	private static HashSet<Node> findUnit(Node startingMethod) {
		//Get a list for all local roots above the given method
		if (startingMethod == null) return null;
		HashSet<Node> localRoots = new HashSet<Node>();
		HashSet<Node> visitedMethods = new HashSet<Node>();
		findAllLocalParentsForMethod(startingMethod, startingMethod, localRoots, visitedMethods);

		int maxUnitSize = 0;
		HashSet<Node> bestUnit = null; 
		
		for (Node localRoot : localRoots) {
			//For each local parent, get all methods it calls.
			HashSet<Node> unit = new HashSet<>();
			findAllDescendantsOfMethod(localRoot, unit);
			//Determine if a local root 
			trimListToAUnit(unit, localRoot);
			//Discard Units that don't include the given method
			if (!unit.contains(startingMethod)) continue;
			//Find the biggest Unit
			if (unit.size() > maxUnitSize) {
				maxUnitSize = unit.size();
				bestUnit = unit;
			}
		}
		return bestUnit;
	}
	
	private static void findAllLocalParentsForMethod(Node startingMethod, Node currentMethod, HashSet<Node> localRoots, HashSet<Node> visitedMethods) {
		if (visitedMethods.contains(currentMethod)) {
			localRoots.add(currentMethod);
			return;
		}
		visitedMethods.add(currentMethod);
		ArrayList<Node> parents = currentMethod.parents;
		if (parents.size() != 1) localRoots.add(currentMethod);
		for (Node parent : parents) {
			findAllLocalParentsForMethod(startingMethod, parent, localRoots, visitedMethods);
		}
	}
	
	private static void findAllDescendantsOfMethod(Node currentMethod, HashSet<Node> descendants){
		if (descendants.contains(currentMethod)) return;
		descendants.add(currentMethod);
		for (Node child : currentMethod.children) {
			findAllDescendantsOfMethod(child, descendants);
		}
	}
	
	/**
	 * A list of nodes is a unit if no node EXCEPT ROOT 
	 * is called by a node outside of the list
	 * To trim a list of nodes to a unit, get all nodes with outside callers
	 * Then, remove those node and all their children from the list
	 * @return
	 */
	private static void trimListToAUnit(HashSet<Node> list, Node root) {
		ArrayList<Node> nodesWithOutsideCallers = new ArrayList<Node>();
		//For all nodes
		for (Node node : list) {
			if (node == root) continue;
			//For each parent of each node
			ArrayList<Node> parents = node.parents;
			for (Node parent : parents) {
				//If the parent is outside the list, this node will need to be removed
				if (!list.contains(parent)) nodesWithOutsideCallers.add(node);
			}
		}
		
		//For all nodes to be removed
		for (Node node : nodesWithOutsideCallers) {
			HashSet<Node> descendants = new HashSet<Node>();
			findAllDescendantsOfMethod(node, descendants);
			list.removeAll(descendants);
		}
		return;
	}
}
