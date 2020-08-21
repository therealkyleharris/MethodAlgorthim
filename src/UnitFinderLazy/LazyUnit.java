package UnitFinderLazy;
import java.util.HashSet;

public class LazyUnit {
    private HashSet<LazyNode> nodes;
    private LazyNode root;

    public LazyUnit(HashSet<LazyNode> nodes, LazyNode root) {
        this.nodes = nodes;
        this.root = root;
    }

    public HashSet<LazyNode> getNodes(){
        return nodes;
    }

    public LazyNode getRoot(){
        return root;
    }

    public boolean areNodesIdentical(LazyUnit unit) {
        return this.nodes.equals(unit.getNodes());
    }
}
