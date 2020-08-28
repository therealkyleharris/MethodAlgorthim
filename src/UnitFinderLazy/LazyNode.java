package UnitFinderLazy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LazyNode {
    public String id;
    private String module = null;
    private ArrayList<LazyNode> parents = null, children = null;
    private LazyDataStore lazyDataStore;

    public LazyNode(String instanceId, LazyDataStore lazyDataStore){
        this.id = instanceId;
        this.lazyDataStore = lazyDataStore;
    }

    public String getModule(){
        if (module != null) {
            return module;
        }
        module = lazyDataStore.retrieveModule(this);
        return module;
    }

    public ArrayList<LazyNode> getParents(){
        if (parents != null){
            return parents;
        }
        parents = lazyDataStore.retrieveParents(this);
        return parents;
    }

    public ArrayList<LazyNode> getChildren(){
        if (children != null){
            return children;
        }
        children = lazyDataStore.retrieveChildren(this);
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
        //return id + " - M:" + getModule() + " - P:" + getParents().size() + ", C:" + getChildren().size();
        return id;
    }
}
