package no.uib.info381.knn.convenience;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.csvreader.CsvReader;


public final class TableHelper {
	
	/***
	 * default reading is unlimited records, shortcut method
	 * @param filePath
	 * @return
	 */
	public static Table ReadTableFromCSV(String filePath) {
		return ReadTableFromCSV(filePath, -1);
	}
	
	/***
	 * read data from csv file and store in table structure
	 * @param filePath
	 * @return
	 */
	public static Table ReadTableFromCSV(String filePath,int maxRecords) {
		Table table = null;
		System.out.println("Reading "+filePath+" and importing as table...");
		try {
			CsvReader dataset = new CsvReader(filePath);
			dataset.readHeaders();
			String[] Attributes = dataset.getHeaders();
			table = new Table(Attributes);
			
			String[] data = new String[Attributes.length];
			int records = 0;
			while (dataset.readRecord() && (records<maxRecords || maxRecords==-1))
			{
				for (int i=0; i<Attributes.length; i++)
					data[i] = dataset.get(Attributes[i]).trim();
				table.Insert(data);
				records++;
			}
			dataset.close();
			System.out.println("Successfully imported "+records+" records.");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return table;
	}
	
	/**
	 * converts list of Integer objects to int[]
	 * @param integers
	 * @return
	 */
	public static int[] convertIntegers(List<Integer> integers)
	{
	    int[] ret = new int[integers.size()];
	    for (int i=0; i < ret.length; i++)
	    {
	        ret[i] = integers.get(i).intValue();
	    }
	    return ret;
	}
}
