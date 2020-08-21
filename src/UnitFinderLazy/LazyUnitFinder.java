package UnitFinderLazy;

import UnitFinder.Node;
import Visualizer.GraphVisualizer;
import Visualizer.LazyGraphVisualizer;
import org.graphstream.graph.Graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class LazyUnitFinder {
    public static LazyDataStore dataStore = null;

    public static void graphUnit(Graph graph, String instanceId){

        try {
            // check if the instance id is valid
            if (dataStore.hasNode(instanceId)) {
                System.out.println("Running on  :" + instanceId);
                LazyNode startingMethod = dataStore.getNode(instanceId);
                LazyUnit unit = findUnit(startingMethod);
                //Unit unitTrimmed = removeExternalParentsAndChildren(unit);
                for (LazyNode node : unit.getNodes()) {
                    System.out.println(node);
                }
                LazyGraphVisualizer.addUnitToGraph(graph, unit);
            }else{
                System.out.println("Invalid Instance ID ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static LazyUnit findUnit(LazyNode startingMethod) {
        //Get a list for all local roots above the given method
        if (startingMethod == null) return null;
        HashSet<LazyNode> localRoots = new HashSet<>();
        HashSet<LazyNode> visitedMethods = new HashSet<>();
        findAllLocalParentsForMethod(startingMethod, startingMethod, localRoots, visitedMethods);

        int maxUnitSize = 0;
        HashSet<LazyNode> bestUnitList = null;
        LazyNode bestRoot = null;

        for (LazyNode localRoot : localRoots) {
            //For each local parent, get all methods it calls.
            HashSet<LazyNode> unitList = new HashSet<>();
            findAllDescendantsOfMethod(localRoot, unitList);
            //Find a unit for a potential root
            trimListToAUnit(unitList, localRoot);
            //Discard Units that don't include the given method
            if (!unitList.contains(startingMethod)) continue;
            //Find the biggest Unit
            if (unitList.size() > maxUnitSize) {
                maxUnitSize = unitList.size();
                bestUnitList = unitList;
                bestRoot = localRoot;
            }
        }

        return bestUnitList == null ? null : new LazyUnit(bestUnitList, bestRoot);
    }

    private static void findAllLocalParentsForMethod(LazyNode startingMethod, LazyNode currentMethod, HashSet<LazyNode> localRoots, HashSet<LazyNode> visitedMethods) {
        if (visitedMethods.contains(currentMethod)) {
            localRoots.add(currentMethod);
            return;
        }
        visitedMethods.add(currentMethod);
        // A node is NOT a local root only when it has a single parent, and that parent is in the same module
        if (!(currentMethod.getParents().size() == 1 && currentMethod.getParentsInTheSameModule().size() == 1)) {
            localRoots.add(currentMethod);
        }
        // Since a unit can't cross a module, do not investigate other modules
        Collection<LazyNode> parents = currentMethod.getParentsInTheSameModule();
        for (LazyNode parent : parents) {
            findAllLocalParentsForMethod(startingMethod, parent, localRoots, visitedMethods);
        }
    }

    /**
     * Find all descendants of a method INCLUDING ITSELF
     * While traversing descendants, do not enter a specific node
     * @param currentMethod
     * @param descendants
     * @param block
     */
    private static void findAllDescendantsOfMethodWithBlock(LazyNode currentMethod, HashSet<LazyNode> descendants, LazyNode block){
        descendants.add(block);
        findAllDescendantsOfMethod(currentMethod, descendants);
        descendants.remove(block);
    }

    /**
     * Return all descendants of a method INCLUDING ITSELF in the same module
     * @param currentMethod
     * @param descendants
     */
    private static void findAllDescendantsOfMethod(LazyNode currentMethod, HashSet<LazyNode> descendants){
        if (descendants.contains(currentMethod)) return;
        descendants.add(currentMethod);
        Collection<LazyNode> children = currentMethod.getChildrenInTheSameModule();
        for (LazyNode child : children) {
            findAllDescendantsOfMethod(child, descendants);
        }
    }

    /**
     * A list of nodes is a unit if no node EXCEPT ROOT
     * is called by a node outside of the list
     * To trim a list of nodes to a unit, get all nodes with outside callers
     * Then, remove those node and all their children from the list
     * @return
     */
    private static void trimListToAUnit(HashSet<LazyNode> list, LazyNode root) {
        HashSet<LazyNode> nodesWithOutsideCallers = new HashSet<>();
        //For all nodes
        for (LazyNode node : list) {
            if (node == root) continue;
            //For each parent of each node
            Collection<LazyNode> parents = node.getParents();
            for (LazyNode parent : parents) {
                //If the parent is outside the list, this node will need to be removed
                if (!list.contains(parent)) nodesWithOutsideCallers.add(node);
            }
        }

        //For all nodes to be removed
        for (LazyNode node : nodesWithOutsideCallers) {
            HashSet<LazyNode> descendants = new HashSet<>();
            //Get all descendants of a node with outside callers
            //BUT do not loop back to the unit root
            findAllDescendantsOfMethodWithBlock(node, descendants, root);
            list.removeAll(descendants);
        }
    }
}
