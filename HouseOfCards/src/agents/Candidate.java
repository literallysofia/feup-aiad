package agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import jade.core.Agent;
import jade.core.behaviours.*;
import agentbehaviours.SendBeliefs;

public class Candidate extends Agent {

	private String id;
	private HashMap<String, Integer> credibility = new HashMap<>();
	private ArrayList<String> states = new ArrayList<String>();
	private HashMap<String, Integer> beliefs = new HashMap<>();
	private ArrayList<HashMap<String,Integer>> profile = new ArrayList<>();




	public Candidate(String id, ArrayList<String> states, ArrayList<String> beliefs) {
		this.id = id;
		
		this.states = states;
		
		this.credibility.put("credibility_" + id.charAt(id.length() - 1), 100);
		
		
		for (int i = 0; i < beliefs.size(); i++) {
			Random rnd = new Random();
			int value = rnd.nextInt(100) + 1;
			this.beliefs.put(beliefs.get(i), value);
		}
		
		this.profile.add(this.beliefs);
		this.profile.add(this.credibility);
		//System.out.println(this.id + "\nBeliefs: " + this.beliefs);
	}

	public String getId() {
		return id;
	}
	
	public ArrayList<String> getStates() {
		return states;
	}

	public void setStates(ArrayList<String> states) {
		this.states = states;
	}
	
	public HashMap<String, Integer> getBeliefs() {
		return beliefs;
	}

	public void setBeliefs(HashMap<String, Integer> beliefs) {
		this.beliefs = beliefs;
	}

	public HashMap<String, Integer> getCredibility() {
		return credibility;
	}

	public void setCredibility(int credibility) {
		this.credibility.put(getLocalName(), credibility);
	}

	public void setup() {
		addBehaviour(new SendBeliefs(this));
	}
	
	public ArrayList<HashMap<String, Integer>> getProfile() {
		return profile;
	}

	public void setProfile(ArrayList<HashMap<String, Integer>> profile) {
		this.profile = profile;
	}

}
