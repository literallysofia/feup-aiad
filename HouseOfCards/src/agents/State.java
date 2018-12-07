package agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class State {
	
	private String name;
	private int population = 0;
	private HashMap<String, Integer> votes = new HashMap();
	private String winner;
	private HashMap<String, Integer> beliefs = new HashMap(); 
	private String mainBelief;
	

	public State(String name) {
		this.name = name;
	}


	public int getPopulation() {
		return population;
	}


	public void setPopulation(int population) {
		this.population = population;
	}


	public HashMap<String, Integer> getVotes() {
		return votes;
	}


	public void setVotes(HashMap<String, Integer> votes) {
		this.votes = votes;
	}


	public HashMap<String, Integer> getBeliefs() {
		return beliefs;
	}


	public void setBeliefs(HashMap<String, Integer> beliefs) {
		this.beliefs = beliefs;
	}
	
	public String getWinner() {
		return winner;
	}


	public void setWinner(String winner) {
		this.winner = winner;
	}


	public String getMainBelief() {
		return mainBelief;
	}


	public void setMainBelief(String mainBelief) {
		this.mainBelief = mainBelief;
	}

	public void addVote(String candidate){
		this.population++;		
		
		if(this.votes.containsKey(candidate)){
			int votes = this.votes.get(candidate);
			int newVotes =  votes + 1;
			this.votes.replace(candidate, newVotes);
		}
		else{
			this.votes.put(candidate, 1);
		}
	}
	
	public void addBeliefs(HashMap<String, ArrayList<Integer>> voterBeliefs){
		
		for (Map.Entry<String, ArrayList<Integer>> entry : voterBeliefs.entrySet()) {
		    String belief = entry.getKey();
		    int value = (int) entry.getValue().get(0) + (entry.getValue().get(0)-entry.getValue().get(1))/2;
		    
		    if(this.beliefs.containsKey(belief)){
				int oldValue = this.beliefs.get(belief);
				int newValue =  oldValue + value;
				this.beliefs.replace(belief, newValue);
			}
			else{
				this.beliefs.put(belief, value);
			}
		}
	}
	
	public void calculateWinner(){
		Map.Entry<String, Integer> maxEntry = null;
		for (Map.Entry<String, Integer> entry : this.votes.entrySet())
		{
		    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
		        maxEntry = entry;
		}
		this.winner = maxEntry.getKey();
	}
	
	public void calculateBelief(){
		Map.Entry<String, Integer> maxEntry = null;
		for (Map.Entry<String, Integer> entry : this.beliefs.entrySet())
		{
		    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
		        maxEntry = entry;
		}
		this.mainBelief= maxEntry.getKey();
	}

}
