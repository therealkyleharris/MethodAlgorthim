package Visualizer;

import java.util.HashMap;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;

import UnitFinder.DataParser;
import UnitFinder.Node;

public class ModuleMapper {
	public static void main(String[] args) {
		try {
			Graph graph = new MultiGraph("unit graph");
			HashMap<String, Node> tree = DataParser.readFile("TimeCoreData.csv", 2, 1, 5);
			GraphVisualizer.addDataToGraph(graph, tree.values(), null);
			graph.display();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Module Mapper Done");
	}
}
