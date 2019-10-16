package Visualizer;
import org.graphstream.graph.*;
import UnitFinder.Node;
import UnitFinder.Unit;
import org.graphstream.graph.implementations.MultiGraph;

public class GraphVisualizer {

    private Graph graph;

    public GraphVisualizer(String instanceID, Unit data){
        /* create a new simple Graph and name it after the instance id of the method
           not the best naming convention, but is fine for now */
        this.graph = new MultiGraph(instanceID);
        this.constructGraph(data);
    }


    private void constructGraph(Unit data) {
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


    public void displayGraph(){
        graph.display();
    }
}
