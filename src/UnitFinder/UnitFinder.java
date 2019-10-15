package UnitFinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class UnitFinder {
	public static void main(String[] args) {
		String instanceId = "";
		Scanner sc = new Scanner(System.in);

		while(!instanceId.equalsIgnoreCase("quit")) {
			System.out.print("Enter Method ID (or quit): ");
			instanceId = sc.nextLine();

			try {
				HashMap<String, Node> tree = DataParser.readFile("TimeCoreData.csv", 2, 1, 5);
				//instanceId = "19$144724";	//get Down Traversal 7(SS)*S, 19$144724
				//instanceId = "19$144756";	//Method Traversal@get Infinite Looping(SS)*S, 19$144756
				//instanceId = "19$144751";	//get Down Traversal -2(SS), 19$144751
				//instanceId = "19$144732";	//get Down Traversal 3(SS)*S, 19$144732
				//instanceId = "19$144727";	//get Down Traversal 5(SS)*S, 19$144727
				//instanceId = "18$77262";	//Former Null Unit
				//instanceId = "26$87467";	//Former Null Unit
				Node startingMethod = tree.get(instanceId);

				Unit unit = findUnit(startingMethod);
				Unit unitTrimmed = removeExternalParentsAndChildren(unit);

				for (Node node:unitTrimmed.getNodes()){
					System.out.println(node);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		sc.close();
		System.out.println("Unit Finder Done");
	}
	
	protected static Unit findUnit(Node startingMethod) {
		//Get a list for all local roots above the given method
		if (startingMethod == null) return null;
		HashSet<Node> localRoots = new HashSet<Node>();
		HashSet<Node> visitedMethods = new HashSet<Node>();
		findAllLocalParentsForMethod(startingMethod, startingMethod, localRoots, visitedMethods);

		int maxUnitSize = 0;
		HashSet<Node> bestUnitList = null;
		Node bestRoot = null;
		
		for (Node localRoot : localRoots) {
			//For each local parent, get all methods it calls.
			HashSet<Node> unitList = new HashSet<>();
			findAllDescendantsOfMethod(localRoot, unitList);
			//Determine if a local root 
			trimListToAUnit(unitList, localRoot);
			//Discard Units that don't include the given method
			if (!unitList.contains(startingMethod)) continue;
			//Find the biggest Unit
			if (unitList.size() > maxUnitSize) {
				maxUnitSize = unitList.size();
				bestUnitList = unitList;
				bestRoot = localRoot;
			}
		}
		
		return bestUnitList == null ? null : new Unit(bestUnitList, bestRoot);
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
	
	/**
	 * Find all descendants of a method INCLUDING ITSELF
	 * While traversing descendants, do not enter a specific note
	 * @param currentMethod
	 * @param descendants
	 * @param block
	 */
	private static void findAllDescendantsOfMethodWithBlock(Node currentMethod, HashSet<Node> descendants, Node block){
		descendants.add(block);
		findAllDescendantsOfMethod(currentMethod, descendants);
		descendants.remove(block);
	}
	
	/**
	 * Return all descendants of a method INCLUDING ITSELF
	 * @param currentMethod
	 * @param descendants
	 */
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
		HashSet<Node> nodesWithOutsideCallers = new HashSet<Node>();
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
			//Get all descendants of a node with outside callers
			//BUT do not loop back to the unit root
			findAllDescendantsOfMethodWithBlock(node, descendants, root);
			list.removeAll(descendants);
		}
		
		return;
	}
	
	/**
	 * Starting with a list of Nodes, return a list of new Nodes that only have parents and children on the original list.
	 * The original Nodes are not modified, so that they can be reused later
	 * @param origList
	 * @return
	 */
	private static Unit removeExternalParentsAndChildren(Unit origUnit) {
		HashSet<Node> origList = origUnit.getNodes();
		HashMap<String, Node> trimmedMap = new HashMap<String, Node>();
		//First pass - duplicate and save nodes against their IDs
		for (Node origNode : origList) {
			Node newNode = new Node(origNode.name, origNode.id);
			trimmedMap.put(newNode.id, newNode);
		}
		
		//Second pass - duplicate Parent and Children lists, but only allow Nodes from the original list
		for (Node origNode : origList) {
			Node newNode = trimmedMap.get(origNode.id);
			for (Node parent : origNode.parents) {
				if (origList.contains(parent)) {
					newNode.parents.add(parent);
				}
			}
			for (Node child : origNode.children) {
				if (origList.contains(child)) {
					newNode.children.add(child);
				}
			}
		}
		HashSet<Node> newList = new HashSet<Node>(trimmedMap.values());
		return new Unit(newList, origUnit.getRoot());
	}
}
