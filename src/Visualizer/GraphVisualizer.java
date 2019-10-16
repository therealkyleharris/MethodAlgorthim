package Visualizer;
import org.graphstream.graph.*;
import UnitFinder.Node;
import UnitFinder.Unit;

public class GraphVisualizer {
    public static void addUnitToGraph(Graph graph, Unit data) {
    	//First Pass - add all nodes:
    	for (Node node : data.getNodes()) {
    		graph.addNode(node.getId());
    		String label = node == data.getRoot() ? "ROOT : " + node.getId() : node.getId();
    		graph.getNode(node.getId()).setAttribute("ui.label", label);
    	}
    	
    	//Second Pass - add all edges
    	for (Node node : data.getNodes()) {
    		for (Node parent : node.getParents()) {
    			String edgeName = (node.getId() + "--" + parent.getId());
    			graph.addEdge(edgeName, parent.getId(),node.getId(), true);
                Edge e = graph.getEdge(edgeName);
                e.setAttribute("directed", true);
    		}
    	}
    }
}
