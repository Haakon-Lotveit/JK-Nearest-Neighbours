package no.uib.info381.knn;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Entry point for the application.
 * This is where you're supposed to launch, and where the Jar-Manifesto must point to.
 * Should not do anything except call the classes that are supposed to do things.
 * @author haakon
 */
public class Main {

	private static Scanner kb = new Scanner(System.in);
	/**
	 * @param args Not read, next iteration, we might let people do stuff like --file "~/datas/csvs/somecsvfile.csv" or something.
	 * 
	 * Yeah, I'm not even going to TRY to do error-handling at this point.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		System.out.printf("Enter filename: ");
		FileReader fr = new FileReader(new File(kb.nextLine()));
		CSVReader reader = new CSVReader(fr);
		List<String[]> csvData = reader.readAll();
		reader.close();
		fr.close();
		
		for(String s : csvData.get(0)){
			System.out.println(s);
		}
	}

}
// /home/haakon/workspace/k-nearest-neighbours/testdata/csv/companies.csv