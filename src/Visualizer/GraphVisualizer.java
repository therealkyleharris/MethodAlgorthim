package Visualizer;
import org.graphstream.graph.*;
import UnitFinder.Node;
import UnitFinder.Unit;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.graph.implementations.MultiGraph;

import java.util.ArrayList;
import java.util.HashSet;



public class GraphVisualizer {

    private Graph graph;
    private Unit data;
    private Node root;

    //



    public GraphVisualizer(String instanceID, Unit data){
        /* create a new simple Graph and name it after the instance id of the method
           not the best naming convention, but is fine for now */
        this.graph = new MultiGraph(instanceID);

        // set the data set equal to what we're passing in
        this.data = data;
        this.root = data.getRoot();
        this.constructGraph();
    }


    private void constructGraph() {

        // this is pretty brute force, but it will work
        for (Node node : this.data.getNodes()) {
            //System.out.println(node.toString());

            ArrayList<Node> children = node.getChildren();

            try {
                this.graph.addNode(node.getId());

                if(node == this.root) {
                    this.graph.getNode(node.getId()).setAttribute("ui.label",("ROOT : " + node.getId()));
                }else{
                    this.graph.getNode(node.getId()).setAttribute("ui.label", node.getId());
                }
            } catch (Exception e) {
                // don't handle this yet. Just testing..
            }
            // for every parent, add an edge
            for (Node parent : node.getParents()) {
                // add an edge
                String edgeName = (node.getId() + "--" + parent.getId());
                // add the parent node to the graph
                try {
                    this.graph.addNode(parent.getId());
                    if(parent.getId()==this.root.getId()){
                        this.graph.getNode(parent.getId()).setAttribute("ui.label", ("ROOT : "+parent.getId()));
                    }else{
                        this.graph.getNode(parent.getId()).setAttribute("ui.label", parent.getId());
                    }
                } catch (Exception e) {
                    // probably trying to add a duplicate node here
                }
                this.graph.addEdge(edgeName, parent.getId(),node.getId(), true);
                Edge e = graph.getEdge(edgeName);
                e.setAttribute("directed", true);
            }

           /* for(Node child: node.getChildren()){
                String edgeName = (node.getId()+"--"+child.getId());
                try {
                    this.graph.addNode(child.getId());
                    this.graph.getNode(child.getId()).setAttribute("ui.label", child.getId());
                }catch(Exception e){
                    // same thing here
                }
                this.graph.addEdge(edgeName, node.getId(), child.getId());
                Edge e = graph.getEdge(edgeName);
                e.isDirected();

                */




        }
    }


    public void displayGraph(){
        this.graph.display();
    }

}
