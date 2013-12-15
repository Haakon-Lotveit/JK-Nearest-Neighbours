package no.uib.info381.knn.convenience;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class CSVNormaliser {
	List<String[]> csvList;
	private int classificationIndex;
	
	public CSVNormaliser(File csvfile, int classificationIndex) throws IOException{
		this.classificationIndex = classificationIndex;
		CSVReader reader = new CSVReader(new FileReader(csvfile));
		csvList = reader.readAll();
	}
	
	/**
	 * Loops over all attributes.
	 * Loop runs in two stages. First stage calculates an average value, second stage inserts it into blank value-fields.
	 */
	public CSVNormaliser fillBlanks(){
		if(csvList.isEmpty()){
			System.err.println("Tried to fill blanks on empty dataset.");
			return this;
		}
		
		int numAttributes = csvList.get(0).length;
		
		for(int attribute = 0; attribute < numAttributes; ++attribute){
			if(attribute == classificationIndex){
				continue; // Skip the classification index. It's just strings anyway.
			}
			double sum = 0.0;
			int num = 0;
			for(String[] row : csvList){				
				if(row[attribute].length() != 0){
					sum += Double.parseDouble(row[attribute]);
					++num;
				}
			}
			double average = sum/num;

			for(String[] row : csvList){
				if(row[attribute].length() == 0){
					row[attribute] = String.valueOf(average);				
				}
			}
		}
		return this;
	}
	
	public CSVNormaliser normalize(){
		if(csvList.isEmpty()){
			System.err.println("Tried to fill blanks on empty dataset.");
			return this;
		}
		
		int numAttributes = csvList.get(0).length;
		
		for(int attribute = 0; attribute < numAttributes; ++attribute){
			if(attribute == classificationIndex){
				continue; // skip the classification. It's not a number so shall not be messed with.
			}
			double largest = Double.MIN_VALUE;
			double lowest = Double.MAX_VALUE;

			for(String[] row : csvList){
				if(Double.parseDouble(row[attribute]) > largest){
					largest = Double.parseDouble(row[attribute]); 
				}
				if(Double.parseDouble(row[attribute]) < lowest){
					lowest = Double.parseDouble(row[attribute]); 
				}
			}
			
			// Nå som vi har største og minste, kan vi bruke formelen normalisert(n) = (n - minste)/(største - minste)
			for(String[] row : csvList){
				row[attribute] = String.valueOf((Double.parseDouble(row[attribute]) - lowest)/(largest - lowest));
			}
		}
		return this;
	}
	
	
	public void fix(){
		this.fillBlanks();
		this.normalize();		
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(String[] row : csvList){
			for(String cell : row){
				sb.append(cell).append(',');
			}
			sb.append('\n');
		}		
		return sb.toString();
	}
}
