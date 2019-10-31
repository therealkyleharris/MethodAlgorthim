package UnitFinder;
import java.util.*;

public class UnitFinder {

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
			//Find a unit for a potential root 
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
		// A node is NOT a local root only when it has a single parent, and that parent is in the same module
		if (!(currentMethod.getParents().size() == 1 && currentMethod.getParentsInTheSameModule().size() == 1)) {
			localRoots.add(currentMethod);
		}
		// Since a unit can't cross a module, do not investigate other modules
		Collection<Node> parents = currentMethod.getParentsInTheSameModule(); 
		for (Node parent : parents) {
			findAllLocalParentsForMethod(startingMethod, parent, localRoots, visitedMethods);
		}
	}
	
	/**
	 * Find all descendants of a method INCLUDING ITSELF
	 * While traversing descendants, do not enter a specific node
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
	 * Return all descendants of a method INCLUDING ITSELF in the same module
	 * @param currentMethod
	 * @param descendants
	 */
	private static void findAllDescendantsOfMethod(Node currentMethod, HashSet<Node> descendants){
		if (descendants.contains(currentMethod)) return;
		descendants.add(currentMethod);
		Collection<Node> children = currentMethod.getChildrenInTheSameModule();
		for (Node child : children) {
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
			Collection<Node> parents = node.getParents();
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
}
