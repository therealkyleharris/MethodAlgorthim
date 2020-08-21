package Visualizer;

import UnitFinderLazy.LazyNode;
import UnitFinderLazy.LazyUnit;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;

import java.util.Collection;
import java.util.Random;

public class LazyGraphVisualizer {
    static Random r = new Random();
    public static String RED = "fill-color: rgb(255, 0, 0);";
    public static String BLACK = "fill-color: rgb(0, 0, 0);";

    public static void addUnitToGraph(Graph graph, LazyUnit unit) {
        addDataToGraph(graph, unit.getNodes(), unit.getRoot());
    }

    public static void addDataToGraph(Graph graph, Collection<LazyNode> nodes, LazyNode root) {

        //double redness = 255*(nodes.size() / 59.0);
        //System.out.println("Redness: "+ red);
        String color = genColor();

        //First Pass - add all nodes:
        //graph.addAttribute("ui.stylesheet", "node { fill-color: red; }");
        for (LazyNode node : nodes) {

            // only add the node if it doesn't exist
            if(graph.getNode(node.id)==null) {
                graph.addNode(node.id);
            }
            String label = node == root ? "ROOT : " + node.id : node.id;
            graph.getNode(node.id).setAttribute("ui.label", label);
            graph.getNode(node.id).setAttribute("ui.style", color);

            // make the nodes a pretty color
        }

        //Second Pass - add all edges. This links nodes in a unit AND links unit to other units already on the screen.
        //Process, both, parent and children links to ensure connections to other units.
        for (LazyNode node : nodes) {
            for (LazyNode parent : node.getParents()) {
                addEdge(graph, parent, node);
            }
            for (LazyNode child : node.getChildren()) {
                addEdge(graph, node, child);
            }
        }
    }

    public static void addEdge(Graph graph, LazyNode parent, LazyNode child) {
        if (graph.getNode(parent.id) == null || graph.getNode(child.id) == null) {
            //If an origin or destination of this edge is not on the graph,
            // as will happen when trying to connect the unit to outside nodes that haven't been added yet,
            // do nothing.
            return;
        }
        String edgeName = (parent.id + "--" + child.id);
        if (graph.getEdge(edgeName) == null) {
            graph.addEdge(edgeName, parent.id, child.id, true);
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
