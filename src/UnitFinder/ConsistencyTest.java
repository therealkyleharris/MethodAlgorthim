package UnitFinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ConsistencyTest {

	static ArrayList<Integer> unit_count;
	
	public static void main(String[] args) {
		runConsistencyTest();
	}

	/**
	 * 1 For each method, get its unit.
	 * 2 For each method in the unit, get it's unit
	 * 3 Verify that the above 2 units are the same
	 * 
	 * In step 1, skip all methods that have already been tested in step 2 
	 * @param args
	 */
	public static void runConsistencyTest(){
		int max_nodes = 0;
		unit_count = new ArrayList<Integer>();

		try {
			//HashMap<String, Node> tree = DataParser.readFile("TimeCoreAndTools.csv");
			HashMap<String, Node> tree = DataParser.readFile("AllTime.csv");
			Set<String> methodIDs = tree.keySet();
			HashSet<String> testedMethodIDs = new HashSet<String>();
			System.out.println("Consistency Check: " + tree.size());
			int count = 0;
			//Graph graph = new MultiGraph("master graph");
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

				if(unit.getNodes().size() > max_nodes){
					max_nodes = unit.getNodes().size();
				}
				// put the unit size in the array list
				unit_count.add(unit.getNodes().size());

				verifyUnitConsistency(methodID, unit);
				for (Node node : unit.getNodes()) {
					testedMethodIDs.add(node.id);
				}				
			}
			//graph.display();
			System.out.println("Consistency Test Done");
			System.out.println("Max nodes : " + max_nodes);
			System.out.println("Standard deviation : " + calculate_standard_deviation());
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

	static double calculate_standard_deviation(){

		int sum = 0;
		for(int x : unit_count ){
			sum+=x;
		}
		int mean = (sum/ unit_count.size());

		int sum_2 = 0;

		for (int x: unit_count){
			sum_2+=Math.pow(x-mean,2);
		}

		return Math.sqrt(sum_2 /unit_count.size());


	}


}
