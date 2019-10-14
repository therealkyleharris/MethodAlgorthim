package DataParser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class DataParser {
	public static void main(String[] args) {
		try {
			readFile("TimeCoreData.csv", 6, 1, 3, 5);
			System.out.println("Done");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void readFile(String filename, int idColumn, int nameColumn, int ascendantColumn, int descendantColumn) throws IOException{
		HashMap<String, Node> tree = new HashMap<>();
		
		File csvData = new File(filename);
		CSVParser parser = CSVParser.parse(csvData, Charset.defaultCharset(), CSVFormat.RFC4180);
		//First pass - 
		boolean firstRow = true;
		for (CSVRecord row : parser) {
			if (firstRow) {
				firstRow = false;
				continue;
			}
			String id = row.get(idColumn);
			String name = row.get(nameColumn);
			String[] ascendantIDs = row.get(ascendantColumn).split(",");
			String[] descendantIDs = row.get(descendantColumn).split(",");
			Node node = new Node(name, id);
			tree.put(id, node);
		}
		return;
	}
}