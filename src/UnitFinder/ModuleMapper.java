package UnitFinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;

import Visualizer.GraphVisualizer;

public class ModuleMapper {


	public static void main(String[] args) {
		try {
			Graph graph = new MultiGraph("unit graph");
			HashMap<String, Node> tree = DataParser.readFile("AllTime.csv");
			mapModule(graph, tree);
			graph.display();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Module Mapper Done");
	}
	
	public static void mapModule(Graph graph, HashMap<String, Node> tree) {
		GraphVisualizer.addDataToGraph(graph, tree.values(), null);
		ArrayList<Unit> nodesByUnit = mapNodesToUnits(tree);
		for (Unit unit : nodesByUnit) {
			String color = GraphVisualizer.genColor();
			for (Node node : unit.getNodes()) {
				graph.getNode(node.getId()).setAttribute("ui.style", color);
			}
			Node root = unit.getRoot();
			graph.getNode(root.getId()).setAttribute("ui.label", "ROOT : " + root.getId());
		}
	}
	
	private static ArrayList<Unit> mapNodesToUnits(HashMap<String, Node> tree) {
		ArrayList<Unit> units = new ArrayList<Unit>();
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
			visitedNodes.addAll(unit.getNodes());
			units.add(unit);
		}
		return units;
	}
}
