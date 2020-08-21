package UnitFinderLazy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LazyNode {

    public String id, name, module;
    private ArrayList<LazyNode> parents = null, children = null;

    public LazyNode(String instanceId, String name, String module){
        this.id = instanceId;
        this.name = name;
        this.module = module;
    }

    public ArrayList<LazyNode> getParents(){
        if (parents != null){
            return parents;
        }
        parents = LazyDataStore.retrieveParents(this);
        return parents;
    }

    public ArrayList<LazyNode> getChildren(){
        if (children != null){
            return children;
        }
        children = LazyDataStore.retrieveChildren(this);
        return children;
    }

    public Collection<LazyNode> getParentsInTheSameModule(){
        return getNodesOfGivenModule(getParents());
    }

    public Collection<LazyNode> getChildrenInTheSameModule(){
        return getNodesOfGivenModule(getChildren());
    }

    private Collection<LazyNode> getNodesOfGivenModule(Collection<LazyNode> nodes){
        Predicate<LazyNode> nodeFilter = item -> item.module.equals(this.module);
        return nodes.stream().filter(nodeFilter).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return id + " - M:" + module + " - P:" + getParents().size() + ", C:" + getChildren().size() + " - " + name;
    }
}
