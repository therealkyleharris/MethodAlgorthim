package UnitFinderLazy;

import UnitFinder.DataParser;
import UnitFinder.Node;

import java.util.HashMap;

public class LazyDataStore {
    private HashMap<String, Node> csvTree;
    private HashMap<String, LazyNode> savedNodes = new HashMap<>();

    public LazyDataStore(HashMap<String, Node> csvTree){
        this.csvTree = csvTree;
    }

    public LazyNode getNode(String instanceId){
        if (savedNodes.containsKey(instanceId))return savedNodes.get(instanceId);
        if (doesNodeExist(instanceId)){
            LazyNode node = new LazyNode(instanceId, csvTree);
            savedNodes.put(instanceId, node);
            return node;
        }
        return null;
    }

    public boolean hasNode(String instanceId){
        return getNode(instanceId) != null;
    }

    private boolean doesNodeExist(String instanceId){
        return csvTree.containsKey(instanceId);
    }
}
