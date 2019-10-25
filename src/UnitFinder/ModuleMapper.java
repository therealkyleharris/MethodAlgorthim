package UnitFinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;

import Visualizer.GraphVisualizer;

public class ModuleMapper {


	public static void main(String[] args) {
		try {
			Graph graph = new MultiGraph("unit graph");
			mapModule(graph);
			graph.display();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Module Mapper Done");
	}
	
	public static void mapModule(Graph graph) {
		HashMap<String, Node> tree = DataParser.readFile("TimeCoreAndTools.csv");
		GraphVisualizer.addDataToGraph(graph, tree.values(), null);
		ArrayList<Set<Node>> nodesByUnit = mapNodesToUnits(tree);
		for (Set<Node> unitNodes : nodesByUnit) {
			String color = GraphVisualizer.genColor();
			for (Node node : unitNodes) {
				graph.getNode(node.getId()).setAttribute("ui.style", color);
			}
		}
	}
	
	private static ArrayList<Set<Node>> mapNodesToUnits(HashMap<String, Node> tree) {
		ArrayList<Set<Node>> units = new ArrayList<Set<Node>>();
		Collection<Node> nodes = tree.values();
		HashSet<Node> visitedNodes = new HashSet<Node>();
		int count = 0;
		System.out.println("Mapping Nodes to Units: " + nodes.size());
		for (Node node : nodes) {
			if (count++%100==0) {
				System.out.println(count-1);
			}
			if (visitedNodes.contains(node)) continue;
			Unit unit = UnitFinder.findUnit(node);
			HashSet<Node> unitNodes = unit.getNodes();
			visitedNodes.addAll(unitNodes);
			units.add(unitNodes);
		}
		return units;
	}
}
