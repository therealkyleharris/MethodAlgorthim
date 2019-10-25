package Visualizer;
import java.util.Collection;
import java.util.Random;
import org.graphstream.graph.*;
import UnitFinder.Node;
import UnitFinder.Unit;


public class GraphVisualizer {

	static Random r = new Random();
	
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
    	String red = "fill-color: rgb(255, 0, 0);";
    	String black = "fill-color: rgb(0, 0, 0);";
    	for (Node node : nodes) {
    		for (Node parent : node.getParents()) {
    			String edgeName = (node.getId() + "--" + parent.getId());
    			try {
					graph.addEdge(edgeName, parent.getId(), node.getId(), true);
				}catch(Exception e){
    				// duplicate edge here, no need to handle this other than catching the exception.
				}
                Edge e = graph.getEdge(edgeName);
                e.setAttribute("directed", true);
                String edgeColor = node.module.equals(parent.module) ? black : red;
                e.setAttribute("ui.style", edgeColor);
    		}
    	}
    }

    public static void expandGraphNode(Graph graph, Node root, Unit unit){
    	// add an edge here
		// maybe
		// ^ remove above comment
    	addUnitToGraph(graph, unit);
		//graph.addEdge("",root.getId(), unit.getRoot().getId());
	}





    // Uniquley colors each unit in the graph
     public static String genColor(){

    	int color_1 = r.nextInt(255);
		int color_2 = r.nextInt(255);
		int color_3 = r.nextInt(255);

		return String.format("fill-color: rgb(%d, %d, %d);", color_1, color_2, color_3);
	}

    //
	/*static String genColor(double redness){

		double rounded_red = Math.floor(redness);
		int color_1 = (int)rounded_red;
		int color_2 = 255-color_1;
		int color_3 = 0;

		if(color_1!=0){
			System.out.println(color_1+ " "+color_2+" "+color_3);
		}


		return String.format("fill-color: rgb(%d, %d, %d);", color_1, color_2, color_3);

	}*/






}
