package UnitFinderLazy;

import UnitFinder.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LazyNode {
    private HashMap<String, Node> csvTree;

    private String id, name = null, module = null;
    private ArrayList<LazyNode> parents = null, children = null;

    public LazyNode(String instanceId, HashMap<String, Node> csvTree){
        this.id = instanceId;
        this.csvTree = csvTree;
    }

    public String getId(){
        return id;
    }

    public String getName(){
        if (name != null){
            return name;
        }
        Node csvNode = csvTree.get(id);
        if (csvNode == null) return null;
        name = csvNode.getName();
        return name;
    }

    public String getModule(){
        if (module != null){
            return module;
        }
        Node csvNode = csvTree.get(id);
        if (csvNode == null) return null;
        module = csvNode.getModule();
        return module;
    }

    public ArrayList<LazyNode> getParents(){
        if (parents != null){
            return parents;
        }
        Node csvNode = csvTree.get(id);
        if (csvNode == null) return null;
        ArrayList<Node> dataStoreParents = csvNode.getParents();
        parents = new ArrayList<>();
        LazyNode parent;
        for (Node dataStoreParent : dataStoreParents){
            parent = new LazyNode(dataStoreParent.getId(), csvTree);
            parents.add(parent);
        }
        return parents;
    }

    public ArrayList<LazyNode> getChildren(){
        if (children != null){
            return children;
        }
        Node csvNode = csvTree.get(id);
        if (csvNode == null) return null;
        ArrayList<Node> dataStoreChildren = csvNode.getChildren();
        children = new ArrayList<>();
        LazyNode child;
        for (Node dataStoreChild : dataStoreChildren){
            child = new LazyNode(dataStoreChild.getId(), csvTree);
            children.add(child);
        }
        return children;
    }

    public Collection<LazyNode> getParentsInTheSameModule(){
        return getNodesOfGivenModule(getParents());
    }

    public Collection<LazyNode> getChildrenInTheSameModule(){
        return getNodesOfGivenModule(getChildren());
    }

    private Collection<LazyNode> getNodesOfGivenModule(Collection<LazyNode> nodes){
        Predicate<LazyNode> nodeFilter = item -> item.getModule().equals(this.getModule());
        return nodes.stream().filter(nodeFilter).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return id + " - M:" + getModule() + " - P:" + getParents().size() + ", C:" + getChildren().size() + " - " + getName();
    }
}
