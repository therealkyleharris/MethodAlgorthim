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
			//get Down Traversal 7(SS)*S, 19$144724
			String startingMethodID = "19$144724";
			Node startingMethod = tree.get(startingMethodID);
			findUnit(startingMethod);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Unit Finder Done");
	}
	
	private static void findUnit(Node startingMethod) {
		if (startingMethod == null) return;
		findAllLocalParentsForMethod(startingMethod);
		return;
	}
	
	private static HashSet<Node> findAllLocalParentsForMethod(Node startingMethod) {
		HashSet<Node> localRoots = new HashSet<Node>();
		findAllLocalParentsForMethodHelper(startingMethod, startingMethod, localRoots);
		return localRoots;
	}
	
	private static void findAllLocalParentsForMethodHelper(Node startingMethod, Node currentMethod, HashSet<Node> localRoots) {
		ArrayList<Node> parents = currentMethod.parents;
		if (parents.size() != 1) localRoots.add(currentMethod);
		for (Node parent : parents) {
			findAllLocalParentsForMethodHelper(startingMethod, parent, localRoots);
		}
	}
}
