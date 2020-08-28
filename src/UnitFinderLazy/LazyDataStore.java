package UnitFinderLazy;

import UnitFinder.Node;

import java.util.ArrayList;
import java.util.HashMap;

public class LazyDataStore {
    public HashMap<String, Node> csvTree;
    private HashMap<String, LazyNode> savedNodes = new HashMap<>();

    public LazyNode getNode(String instanceId){
        if (savedNodes.containsKey(instanceId))return savedNodes.get(instanceId);
        Node dataNode = csvTree.get(instanceId);
        if (dataNode == null) return null;
        LazyNode node = new LazyNode(instanceId, this);
        savedNodes.put(instanceId, node);
        System.out.println("Create node " + instanceId);
        return node;
    }

    public String retrieveModule(LazyNode node){
        Node csvNode = csvTree.get(node.id);
        if (csvNode == null) return null;
        return csvNode.getModule();
    }

    public ArrayList<LazyNode> retrieveParents(LazyNode node){
        Node csvNode = csvTree.get(node.id);
        if (csvNode == null) return null;
        ArrayList<Node> dataStoreParents = csvNode.getParents();
        ArrayList<LazyNode> parents = new ArrayList<>();
        LazyNode parent;
        for (Node dataStoreParent : dataStoreParents){
            parent = getNode(dataStoreParent.getId());
            parents.add(parent);
        }
        return parents;
    }

    public ArrayList<LazyNode> retrieveChildren(LazyNode node){
        Node csvNode = csvTree.get(node.id);
        if (csvNode == null) return null;
        ArrayList<Node> dataStoreChildren = csvNode.getChildren();
        ArrayList<LazyNode> children = new ArrayList<>();
        LazyNode child;
        for (Node dataStoreChild : dataStoreChildren){
            child = getNode(dataStoreChild.getId());
            children.add(child);
        }
        return children;
    }
}
