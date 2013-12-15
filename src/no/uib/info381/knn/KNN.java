package no.uib.info381.knn;

import java.util.List;
import java.util.PriorityQueue;

import no.uib.info381.knn.convenience.Voter;
import no.uib.info381.knn.dataloaders.CSVData;
import no.uib.info381.knn.dataloaders.CSVDataDistanceComparator;

/**
 * Denne funker hvis og bare hvis det ikke kan være noe uavgjort.
 * Så dersom det bare er to klasser å velge mellom og vi velger et oddetall, vil alt være greit.
 * Ellers må vi begynne med å gjøre ting mer ordentlig, og begynne med at nærmeste teller ekstra eller noe.
 * @author Haakon Løtveit
 *
 */
public class KNN {
	private List<CSVData> dataset;
	private Integer[] allowableIndexes;

	public KNN(List<CSVData> dataset){
		this.dataset = dataset;
		Integer numIndexes = dataset.get(0).size();
		allowableIndexes = new Integer[numIndexes];
		for(int i = 0; i < numIndexes; ++i){
			allowableIndexes[i] = i;
		}
	}
	
	/***
	 * classification method used to classify a single object
	 * @param unknown - the object to be classified
	 * @param k - how many neighbors
	 * @param neighbors - this is actually an output, so feed in an empty list thanks (used for visualizing neighbors)
	 * @return
	 */
	public String classify(CSVData unknown, int k, List<CSVData> neighbors){
		CSVDataDistanceComparator comparator = new CSVDataDistanceComparator(unknown, allowableIndexes);
		Voter poll = new Voter();

		PriorityQueue<CSVData> heap = new PriorityQueue<>(dataset.size(), comparator);
		heap.addAll(dataset);
		
		for(int i = 0; i < k; ++i){
			CSVData neighbor = heap.remove(); 
			if (neighbors!=null)
				neighbors.add(neighbor);
			poll.voteFor(neighbor.classification());
		}
		
		return poll.getLeadingCandidate();
		
	}
	
	/***
	 * shortcut method of the method above
	 * @param unknown
	 * @param k
	 * @return
	 */
	public String classify(CSVData unknown, int k) {
		return this.classify(unknown, k, null);
	}
	
	public void addToDataset(CSVData newbie){
		this.dataset.add(newbie);
	}
	
	public void useIndexes(Integer[] allowable){
		this.allowableIndexes = allowable;
	}
}
