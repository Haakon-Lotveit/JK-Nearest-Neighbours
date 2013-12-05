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
import no.uib.info381.knn.dataloaders.CSVDataDistanceComparator;
import no.uib.info381.knn.userinterface.cli.CommandLine;

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
	 * @param args Not read, next iteration, we might let people do stuff like --file "~/datas/csvs/somecsvfile.csv" --classify 45 23 543 123 UNKNOWN or something.
	 * 
	 * Yeah, I'm not even going to TRY to do error-handling at this point.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		CommandLine userInterface = new CommandLine();
		userInterface.start();

		// HELE DENNE GREIEN ER BARE TESTING FRA TIDLIGERE. DET SKAL OVER I JUNIT.
//		int classifierIndex = 7; // Indeksen til feltet som skal brukes som en klasse.
//		System.out.printf("Enter filename: ");
//		FileReader fr = new FileReader(new File(kb.nextLine().trim()));
//		CSVReader reader = new CSVReader(fr);
//		List<String[]> csvData = reader.readAll();
//		reader.close();
//		fr.close();
//		
//		System.out.println("CSV-fil lest. Oppretter objekter");
//		
//		List<CSVData> objekter = new LinkedList<>();
//		
//		for(String[] rad : csvData){
//			objekter.add(new Adult(rad, classifierIndex));
//		}
//		
//		System.out.printf("Lagde %d objekter.%n", objekter.size());
//		
		
		/* Diverse testemetoder */
//		System.out.printf("Avstanden mellom de to første er %f%n", objekter.get(0).distanceTo(objekter.get(1)));
//		System.out.printf("Avstanden mellom første og tredje er %f%n", objekter.get(0).distanceTo(objekter.get(2)));
//		System.out.printf("Avstanden mellom første og første er %f%n", objekter.get(0).distanceTo(objekter.get(0)));
//		CSVDataDistanceComparator comp = new CSVDataDistanceComparator(objekter.get(0));
//		int comparison = comp.compare(objekter.get(1), objekter.get(2));
//		System.out.printf("Er avstanden mellom første og andre objekt mindre enn avstanden mellom første og tredje? %s.%n", comparison == 0? "De er like langt unna" : (comparison < 0? "Andre er nærmest" : "Tredje er nærmest"));
		
//		System.out.println("Trying to classify test subject 1");
//		CSVData testSubject = objekter.remove(0);
//		KNN testData = new KNN(objekter);
//		String guess = testData.classify(testSubject, 5);
//		System.out.printf("Actual classification: %s, Guesstimate by KNN: %s%n", testSubject.classification(), guess);
//		
	}

}
// /home/haakon/workspace/k-nearest-neighbours/testdata/csv/companies.csv
// /home/haakon/workspace/k-nearest-neighbours/testdata/csv/adult-removed.csv