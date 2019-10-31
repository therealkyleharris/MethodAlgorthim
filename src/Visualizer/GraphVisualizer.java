package Visualizer;
import java.util.Collection;
import java.util.Random;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.MultiGraph;

import UnitFinder.Node;
import UnitFinder.Unit;


public class GraphVisualizer {

	static Random r = new Random();
	public static String RED = "fill-color: rgb(255, 0, 0);";
	public static String BLACK = "fill-color: rgb(0, 0, 0);";

	public static Graph createGraph() {
		Graph graph = new MultiGraph("unit graph");
		graph.setAttribute("layout.stabilization-limit", 0.9);
		return graph;
	}
	
    public static void addUnitToGraph(Graph graph, Unit unit) {
    	addDataToGraph(graph, unit.getNodes(), unit.getRoot());
    }
    
    public static void addDataToGraph(Graph graph, Collection<Node> nodes, Node root) {

		//double redness = 255*(nodes.size() / 59.0);
		//System.out.println("Redness: "+ red);
		String color = genColor();

    	//First Pass - add all nodes:
    	//graph.addAttribute("ui.stylesheet", "node { fill-color: red; }");
    	for (Node node : nodes) {

    		// only add the node if it doesn't exist
    		if(graph.getNode(node.getId())==null) {
				graph.addNode(node.getId());
			}
    		String label = node == root ? "ROOT : " + node.getId() : node.getId();
    		graph.getNode(node.getId()).setAttribute("ui.label", label);
			graph.getNode(node.getId()).setAttribute("ui.style", color);

			// make the nodes a pretty color
    	}
    	
    	//Second Pass - add all edges
    	for (Node node : nodes) {
    		for (Node parent : node.getParents()) {
    			addEdge(graph, parent, node);
    		}
    		for (Node child : node.getChildren()) {
    			addEdge(graph, node, child);
    		}
    	}
    }
    
    public static void addEdge(Graph graph, Node parent, Node child) {
    	if (graph.getNode(parent.id) == null || graph.getNode(child.id) == null) {
    		//If an origin or destination of this edge is not on the graph,
    		// as will happen when trying to connect the unit to outside nodes that haven't been added yet,
    		// do nothing.
    		return;
    	}
    	String edgeName = (parent.getId() + "--" + child.getId());
    	if (graph.getEdge(edgeName) == null) {
    		graph.addEdge(edgeName, parent.getId(), child.getId(), true);
            Edge e = graph.getEdge(edgeName);
            e.setAttribute("directed", true);
            String edgeColor = parent.module.equals(child.module) ? BLACK : RED;
            e.setAttribute("ui.style", edgeColor);
    	}
    }

    // Uniquley colors each unit in the graph
    public static String genColor(){
    	int color_1 = r.nextInt(255);
		int color_2 = r.nextInt(255);
		int color_3 = r.nextInt(255);
		return String.format("fill-color: rgb(%d, %d, %d);", color_1, color_2, color_3);
	}
}
