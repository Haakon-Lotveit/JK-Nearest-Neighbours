package no.uib.info381.knn.dataloaders;

import java.util.Comparator;

public class CSVDataDistanceComparator implements Comparator<CSVData> {
	CSVData comparisonBase;
	public CSVDataDistanceComparator(CSVData compareBasedOnMe){
		this.comparisonBase = compareBasedOnMe;
	}
	
	/**
	 * Dersom o1 er nærmere comparisonBase returnerer vi et negativt tall.
	 * Dersom o2 er nærmere comparisonBase returnerer vi et positivt tall.
	 * Ellers returnerer vi 0, da er de like langt unna. (Merk at dette ikke er helt sannsynlig, siden vi  bruker flyttall) 
	 */
	@Override
	public int compare(CSVData o1, CSVData o2) {
		double a = comparisonBase.distanceTo(o1);
		double b = comparisonBase.distanceTo(o2);		
		return (int) Math.round(a - b);
	}

}
