package Visualizer;
import java.util.Collection;
import java.util.Set;

import org.graphstream.graph.*;
import UnitFinder.Node;
import UnitFinder.Unit;

public class GraphVisualizer {
	
    public static void addUnitToGraph(Graph graph, Unit unit) {
    	addDataToGraph(graph, unit.getNodes(), unit.getRoot());
    }
    
    public static void addDataToGraph(Graph graph, Collection<Node> nodes, Node root) {
    	//First Pass - add all nodes:
    	//graph.addAttribute("ui.stylesheet", "node { fill-color: red; }");
    	for (Node node : nodes) {
    		graph.addNode(node.getId());
    		String label = node == root ? "ROOT : " + node.getId() : node.getId();
    		graph.getNode(node.getId()).setAttribute("ui.label", label);
    	}
    	
    	//Second Pass - add all edges
    	for (Node node : nodes) {
    		for (Node parent : node.getParents()) {
    			String edgeName = (node.getId() + "--" + parent.getId());
    			graph.addEdge(edgeName, parent.getId(),node.getId(), true);
                Edge e = graph.getEdge(edgeName);
                e.setAttribute("directed", true);
    		}
    	}
    }
}
