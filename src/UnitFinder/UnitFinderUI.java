package UnitFinder;
import Visualizer.GraphVisualizer;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.MultiNode;
import org.graphstream.ui.view.Viewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import static UnitFinder.UnitFinder.findUnit;
import static UnitFinder.UnitFinder.removeExternalParentsAndChildren;

public class UnitFinderUI {

    //instanceId = "19$144724";	//get Down Traversal 7(SS)*S, 19$144724
    //instanceId = "19$144756";	//Method Traversal@get Infinite Looping(SS)*S, 19$144756
    //instanceId = "19$144751";	//get Down Traversal -2(SS), 19$144751
    //instanceId = "19$144732";	//get Down Traversal 3(SS)*S, 19$144732
    //instanceId = "19$144727";	//get Down Traversal 5(SS)*S, 19$144727
    //instanceId = "18$77262";	//Former Null Unit
    //instanceId = "26$87467";	//Former Null Unit


    static Scanner sc = new Scanner(System.in);
    private static HashMap<String, Node> tree;
    static boolean graphDisplayed = false;

    static Viewer viewer;


    public static void main(String[] args) {

        displayUI();
    }

    public static void displayUI() {

        Graph graph = new MultiGraph("unit graph");
        boolean interacting = true;
        while (interacting) {
        	tree = DataParser.readFile("AllTime.csv");
        	
            System.out.println("[1] Graph Unit");
            System.out.println("[2] Expand Node");
            System.out.println("[3] Expand All On-Screen Nodes");
            System.out.println("[4] Graph Module");
            System.out.println("[5] Quit");

            String input = sc.nextLine();

            // just want to display the graph once after initial input
            if(!graphDisplayed){
                viewer = graph.display();
                graphDisplayed=true;
            }

            if (input.equalsIgnoreCase("1")) {
                System.out.print("\t Enter an Instance ID : ");
                String instanceId = sc.nextLine();
                graphUnit(graph, instanceId);
                // display the graph
            } else if (input.equalsIgnoreCase("2")){
                System.out.print("Enter a node to expand : " );
                String expandNode=sc.nextLine();
                expandNode(graph,expandNode);
            } else if (input.equalsIgnoreCase("3")){
            	@SuppressWarnings("unchecked")
				Iterable<MultiNode> graphedNodes = (Iterable<MultiNode>) graph.getEachNode();
            	for (MultiNode graphedNode : graphedNodes) {
            		expandNode(graph, graphedNode.getId());
            	}
            } else if (input.equalsIgnoreCase("4")){
            	ModuleMapper.mapModule(graph);
            } else if(input.equalsIgnoreCase("5")){
                viewer.close();
                sc.close();
                // break out of all execution, including main thread and AWT.
                // can cause problems if this is not the only thing executing on the JVM.
                System.exit(0);
            }

        }

    }

    /* This will not fail, but it won't be quite right when there are multiple children in the same unit
     * Need some slight refactoring*/
    private static void expandNode(Graph graph, String instanceId){
        System.out.println("Expand Node code");
        // get the children of the current node
        ArrayList <Node> children = tree.get(instanceId).children;
        // for all the children, graph the units

        for(Node node: children) {
            graphUnit(graph, node.getId());

            String edgeName = instanceId+"->"+node.getId();
            // now add an edge between the source and the child node

            // in some cases an edge will already exist in the current unit
            // adding an edge in that case will fail..
            if(graph.getEdge(edgeName) == null) {
                graph.addEdge(edgeName, instanceId, node.getId(), true);
            }
        }
    }

    private static void graphUnit(Graph graph, String instanceId){

        try {
            // check if the instance id is valid
            if (tree.containsKey(instanceId)) {

                System.out.println("Running on  :" + instanceId);

                Node startingMethod = tree.get(instanceId);

                Unit unit = findUnit(startingMethod);
                Unit unitTrimmed = removeExternalParentsAndChildren(unit);

                for (Node node : unitTrimmed.getNodes()) {
                    System.out.println(node);
                }

                GraphVisualizer.addUnitToGraph(graph, unitTrimmed);

            }else{
                System.out.println("Invalid Instance ID ");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
