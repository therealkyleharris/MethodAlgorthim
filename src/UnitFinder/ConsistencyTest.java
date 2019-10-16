package UnitFinder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;

import Visualizer.GraphVisualizer;

public class ConsistencyTest {
	/**
	 * 1 For each method, get its unit.
	 * 2 For each method in the unit, get it's unit
	 * 3 Verify that the above 2 units are the same
	 * 
	 * In step 1, skip all methods that have already been tested in step 2 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			HashMap<String, Node> tree = DataParser.readFile("TimeCoreData.csv", 2, 1, 5);
			Set<String> methodIDs = tree.keySet();
			HashSet<String> testedMethodIDs = new HashSet<String>();
			System.out.println("Size = " + tree.size());
			int count = 0;
			Graph graph = new MultiGraph("master graph");
			for (String methodID : methodIDs) {
				if (count++%100==0) {
					System.out.println(count-1);
				}
				if (testedMethodIDs.contains(methodID)) {
					//System.out.println("Skip");
					continue;
				}
				Node method = tree.get(methodID);
				Unit unit = UnitFinder.findUnit(method);
				verifyUnitConsistency(methodID, unit);
				for (Node node : unit.getNodes()) {
					testedMethodIDs.add(node.id);
				}
				
				Unit trimmed = UnitFinder.removeExternalParentsAndChildren(unit);
				GraphVisualizer.addUnitToGraph(graph, trimmed);
			}
			graph.display();
			System.out.println("Consistency Test Done");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static void verifyUnitConsistency(String originalMethodID, Unit unit) throws Exception{
		if (unit == null) {
			throw new Exception("Method " + originalMethodID + " returned null Unit");
		}
		for (Node node : unit.getNodes()) {
			Unit comparisonUnit = UnitFinder.findUnit(node);
			if (!unit.areNodesIdentical(comparisonUnit)) {
				throw new Exception("Inconsistency Found Between Units For " + originalMethodID + " and " + node.id);
			}
		}
	}
}
