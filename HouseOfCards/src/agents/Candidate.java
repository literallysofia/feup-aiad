package agents;

import java.util.ArrayList;
import java.util.HashMap;

public class Candidate {

	private HashMap<String, Float> beliefs = new HashMap<>(); 
	private int credibility=100; 
	
	public Candidate() {
		// TODO Auto-generated constructor stub
	}

	public HashMap<String, Float> getBeliefs() {
		return beliefs;
	}

	public void setBeliefs(HashMap<String, Float> beliefs) {
		this.beliefs = beliefs;
	}

	public int getCredibility() {
		return credibility;
	}

	public void setCredibility(int credibility) {
		this.credibility = credibility;
	}

}
