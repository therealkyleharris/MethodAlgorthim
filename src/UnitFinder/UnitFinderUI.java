package UnitFinder;
import Visualizer.GraphVisualizer;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiNode;
import org.graphstream.ui.view.Viewer;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import static UnitFinder.UnitFinder.findUnit;

public class UnitFinderUI {

    //instanceId = "19$144724";	//get Down Traversal 7(SS)*S, 19$144724
    //instanceId = "19$144756";	//Method Traversal@get Infinite Looping(SS)*S, 19$144756
    //instanceId = "19$144751";	//get Down Traversal -2(SS), 19$144751
    //instanceId = "19$144732";	//get Down Traversal 3(SS)*S, 19$144732
    //instanceId = "19$144727";	//get Down Traversal 5(SS)*S, 19$144727
    //instanceId = "18$77262";	//Former Null Unit
    //instanceId = "26$87467";	//Former Null Unit
	//Method Traversal@get Module Break Test 2(SS)*S, 19$145176
	//Method Traversal@get Module Break Test EX-to-3(SS)*S, 19$145178

    private static HashMap<String, Node> tree = DataParser.readFile("AllTime.csv");
    private static Graph graph = GraphVisualizer.createGraph();

    public static void main(String[] args) {
        Viewer viewer = graph.display();
        System.out.println("layout.stabilization-limit: " + graph.getAttribute("layout.stabilization-limit"));
        
        while (true) {        	
            System.out.println("[1] Graph Unit");
            System.out.println("[2] Expand Children Of Node");
            System.out.println("[3] Expand Children of All On-Screen Nodes");
            System.out.println("[4] Expand Parents Of Node");
            System.out.println("[5] Expand Parents of All On-Screen Nodes");
            System.out.println("[6] Graph Module");
            System.out.println("[7] Module Data Consistency Check");
            System.out.println("[8] Quit");

            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine();

            if (input.equalsIgnoreCase("1")) {
                System.out.print("\t Enter an Instance ID : ");
                String instanceId = sanitizeMethodInput(sc);
                graphUnit(instanceId);
            } else if (input.equalsIgnoreCase("2")){
                System.out.print("Enter a node to expand children: " );
                String expandNode=sanitizeMethodInput(sc);
                expandNodeChildren(expandNode, getAllGraphedNodeIDs());
            } else if (input.equalsIgnoreCase("3")){
            	HashSet<String> graphedNodeIDs = getAllGraphedNodeIDs();
            	for (String nodeID : graphedNodeIDs) {
            		expandNodeChildren(nodeID, graphedNodeIDs);
            	}
            } else if (input.equalsIgnoreCase("4")){
                System.out.print("Enter a node to expand parents: " );
                String expandNode=sanitizeMethodInput(sc);
                expandNodeParents(expandNode, getAllGraphedNodeIDs());
            } else if (input.equalsIgnoreCase("5")){
            	HashSet<String> graphedNodeIDs = getAllGraphedNodeIDs();
            	for (String nodeID : graphedNodeIDs) {
            		expandNodeParents(nodeID, graphedNodeIDs);
            	}
            } else if (input.equalsIgnoreCase("6")){
            	mapModule(graph, tree);
            } else if (input.equalsIgnoreCase("7")){
            	ConsistencyTest.runConsistencyTest(tree);
            } else if(input.equalsIgnoreCase("8")){
                viewer.close();
                sc.close();
                // break out of all execution, including main thread and AWT.
                // can cause problems if this is not the only thing executing on the JVM.
                System.exit(0);
            }
        }
    }
    
    private static String sanitizeMethodInput(Scanner sc) {
    	String str = sc.nextLine().trim().replace('.', '$');
    	return str;
    }
    
    private static HashSet<String> getAllGraphedNodeIDs(){
    	@SuppressWarnings("unchecked")
		Iterable<MultiNode> graphedNodes = (Iterable<MultiNode>) graph.getEachNode();
    	HashSet<String> nodeIDs = new HashSet<>();
    	for (MultiNode graphedNode : graphedNodes) {
    		nodeIDs.add(graphedNode.getId());
    	}
    	return nodeIDs;
    }

    /* This will not fail, but it won't be quite right when there are multiple children in the same unit
     * Need some slight refactoring */
    private static void expandNodeChildren(String instanceId, HashSet<String> alreadyDisplayedNodes){
        System.out.println("Expand Node Children " + instanceId);
        Node expandingNode = tree.get(instanceId);
        // get the children of the current node and graph their units
        for(Node child : expandingNode.children) {
        	if (alreadyDisplayedNodes.contains(child.getId())) continue;
            graphUnit(child.getId());
            // now add an edge between the source and the child node
            //GraphVisualizer.addEdge(graph, expandingNode, child);
        }
    }
    
    private static void expandNodeParents(String instanceId, HashSet<String> alreadyDisplayedNodes){
        System.out.println("Expand Node Parents " + instanceId);
        Node expandingNode = tree.get(instanceId);
        // get the children of the current node and graph their units
        for(Node parent : expandingNode.parents) {
        	if (alreadyDisplayedNodes.contains(parent.getId())) continue;
            graphUnit(parent.getId());
            // now add an edge between the source and the child node
            //GraphVisualizer.addEdge(graph, expandingNode, child);
        }
    }


    private static void graphUnit(String instanceId){

        try {
            // check if the instance id is valid
            if (tree.containsKey(instanceId)) {
                System.out.println("Running on  :" + instanceId);
                Node startingMethod = tree.get(instanceId);
                Unit unit = findUnit(startingMethod);
                //Unit unitTrimmed = removeExternalParentsAndChildren(unit);
                for (Node node : unit.getNodes()) {
                    System.out.println(node);
                }
                GraphVisualizer.addUnitToGraph(graph, unit);
            }else{
                System.out.println("Invalid Instance ID ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
	private static void mapModule(Graph graph, HashMap<String, Node> tree) {
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
			GraphVisualizer.addDataToGraph(graph, unit.getNodes(), unit.getRoot());
		}
	}
}
