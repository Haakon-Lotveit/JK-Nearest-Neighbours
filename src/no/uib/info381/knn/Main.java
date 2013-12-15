package no.uib.info381.knn;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import no.uib.info381.knn.convenience.CSVNormaliser;
import no.uib.info381.knn.userinterface.cli.CommandLine;
import no.uib.info381.knn.visualizer.*;

/**
 * Entry point for the application.
 * This is where you're supposed to launch, and where the Jar-Manifesto must point to.
 * Should not do anything except call the classes that are supposed to do things.
 * @author haakon
 */
public class Main {

	/**
	 * @param args Not read, next iteration, we might let people do stuff like --file "~/datas/csvs/somecsvfile.csv" --classify 45 23 543 123 UNKNOWN or something.
	 * 
	 * Yeah, I'm not even going to TRY to do error-handling at this point.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		CommandLine userInterface = new CommandLine();
		//userInterface.start(); 
		CSVNormaliser norm = new CSVNormaliser(new File("testdata/csv/missingdata.csv"), 1);
		System.out.println(norm);
		System.out.println(norm.fillBlanks());
		System.out.println(norm.normalize());
		
//khoa test:
		/*
		Scanner kb = new Scanner(System.in);
		Table dataset = TableHelper.ReadTableFromCSV(kb.nextLine().trim());
		dataset.print(13,300);*/
		// HELE DENNE GREIEN ER BARE TESTING FRA TIDLIGERE. DET SKAL OVER I JUNIT.

	}

}
// /home/haakon/workspace/k-nearest-neighbours/testdata/csv/companies.csv
// :load /home/haakon/workspace/k-nearest-neighbours/testdata/csv/adult-removed.csv 7
// :classify 11 39 77516 13 1 2174 0 40 -print
