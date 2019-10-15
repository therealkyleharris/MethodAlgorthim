package UnitFinder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class DataParser {

	
	public static HashMap<String, Node> readFile(String filename, int idColumn, int nameColumn, int childColumn) throws IOException{
		HashMap<String, Node> tree = new HashMap<>();
		
		File csvData = new File(filename);
		CSVParser parser = CSVParser.parse(csvData, Charset.defaultCharset(), CSVFormat.RFC4180);
		//First pass - create Nodes for all Nodes
		boolean firstRow = true;
		for (CSVRecord row : parser) {
			if (firstRow) {
				firstRow = false;
				continue;
			}
			String id = row.get(idColumn);
			String name = row.get(nameColumn);
			Node node = new Node(name, id);
			tree.put(id, node);
		}
		//Second Pass - link descendants to methods
		parser = CSVParser.parse(csvData, Charset.defaultCharset(), CSVFormat.RFC4180);
		for (CSVRecord row : parser) {
			String id = row.get(idColumn);
			String[] childIDs = row.get(childColumn).split(",");
			for (String childID : childIDs) {
				Node parent = tree.get(id);
				Node child = tree.get(childID);
				if (child == null) continue;
				parent.getChildren().add(child);
				child.getParents().add(parent);
			}
		}
		return tree;
	}
}