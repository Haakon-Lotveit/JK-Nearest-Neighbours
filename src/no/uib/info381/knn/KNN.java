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

	public KNN(List<CSVData> dataset){
		this.dataset = dataset;		
	}
	
	public String classify(CSVData unknown, int k){
		CSVDataDistanceComparator comparator = new CSVDataDistanceComparator(unknown);
		Voter poll = new Voter();

		PriorityQueue<CSVData> heap = new PriorityQueue<>(dataset.size(), comparator);
		heap.addAll(dataset);
		
		for(int i = 0; i < k; ++i){
			poll.voteFor(heap.remove().classification());
		}
		
		return poll.getLeadingCandidate();
		
	}
	
	public void addToDataset(CSVData newbie){
		this.dataset.add(newbie);
	}
}
