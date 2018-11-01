package agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import jade.core.Agent;
import jade.core.behaviours.*;
import agentbehaviours.Asking;
import agentbehaviours.SendBeliefs;

public class Candidate extends Agent {

	private String id;
	private int credibility = 100;
	private ArrayList<String> states = new ArrayList<String>();
	private HashMap<String, Integer> beliefs = new HashMap<>();


	public Candidate(String id, ArrayList<String> states, ArrayList<String> beliefs) {
		this.id = id;
		
		this.states = states;

		for (int i = 0; i < beliefs.size(); i++) {
			Random rnd = new Random();
			int value = rnd.nextInt(100) + 1;
			this.beliefs.put(beliefs.get(i), value);
		}
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

	public int getCredibility() {
		return credibility;
	}

	public void setCredibility(int credibility) {
		this.credibility = credibility;
	}

	public void setup() {
		addBehaviour(new SendBeliefs(this));
	}

}
