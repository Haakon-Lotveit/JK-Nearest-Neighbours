package no.uib.info381.knn.convenience;

import java.util.HashMap;

/**
 * A simple convenience class that lets the k-nn vote for the nearest neighbour without us having to do manual hashmap lookups and all that stuff.
 * @author Haakon Løtveit
 */
public class Voter {
	HashMap<String, Integer> poll;
	String largest;
	Integer numVotes;
	Integer totalVotes;
	
	public Voter(){
		this.poll = new HashMap<>();
		this.largest = "No Votes has been cast";
		this.numVotes = -1;
		this.totalVotes = 0;
	}
	
	public void voteFor(String candidate){
		if(poll.containsKey(candidate)){
			poll.put(candidate, poll.get(candidate) + 1);
		}
		else{
			poll.put(candidate, 1);
		}
		
		if(numVotes < poll.get(candidate)){
			numVotes = poll.get(candidate);
			largest = candidate;
		}
		
		++totalVotes;
	}
	
	public String getLeadingCandidate(){
		return largest;
	}
	public Integer getLeadingVotes(){
		return numVotes;
	}
	
	public Integer getTotalVotes(){
		return totalVotes;
	}
}
