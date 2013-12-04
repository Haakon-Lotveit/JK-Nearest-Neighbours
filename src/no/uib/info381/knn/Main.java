package no.uib.info381.knn;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import no.uib.info381.knn.dataloaders.Adult;
import no.uib.info381.knn.dataloaders.CSVData;

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
		int classifierIndex = 7; // Indeksen til feltet som skal brukes som en klasse.
		System.out.printf("Enter filename: ");
		FileReader fr = new FileReader(new File(kb.nextLine().trim()));
		CSVReader reader = new CSVReader(fr);
		List<String[]> csvData = reader.readAll();
		reader.close();
		fr.close();
		
		System.out.println("CSV-fil lest. Oppretter objekter");
		
		List<Adult> objekter = new LinkedList<>();
		
		for(String[] rad : csvData){
			objekter.add(new Adult(rad, classifierIndex));
		}
		
		System.out.printf("Lagde %d objekter.%n", objekter.size());

	}

}
// /home/haakon/workspace/k-nearest-neighbours/testdata/csv/companies.csv
// /home/haakon/workspace/k-nearest-neighbours/testdata/csv/adult-removed.csv