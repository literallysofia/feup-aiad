package agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import agentbehaviours.IsStaffFinished;
import agentbehaviours.SendBeliefs;
import agentbehaviours.WhatToChange;

public class Candidate extends Agent {

	private String id;
	private int credibility =  60;
	private ArrayList<String> states = new ArrayList<String>();
	private HashMap<String, Integer> beliefs = new HashMap<>();
	private ArrayList<HashMap<String,Integer>> profile = new ArrayList<>();
	private ArrayList<String> chiefsOfStaff = new ArrayList<>();
	
	
	public Candidate(String id, ArrayList<String> states, ArrayList<String> beliefs) {
		this.id = id;
		
		this.states = states;		
		
		for (int i = 0; i < beliefs.size(); i++) {
			Random rnd = new Random();
			int value = rnd.nextInt(100) + 1;
			this.beliefs.put(beliefs.get(i), value);
		}
		
		HashMap<String, Integer> credibilityHash = new HashMap<>();
		credibilityHash.put("Credibility", this.credibility);
		
		this.profile.add(this.beliefs);
		this.profile.add(credibilityHash);
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

	public void setup() {
		System.out.println(" > CANDIDATE: " + this.getLocalName() + " BELIEFS: " + this.beliefs);
		SequentialBehaviour trial = new SequentialBehaviour();
		addBehaviour(new SendBeliefs(this));
		trial.addSubBehaviour(new IsStaffFinished(this));
		trial.addSubBehaviour(new WhatToChange(this,new ACLMessage(ACLMessage.CFP)));
		addBehaviour(trial);
	}
	
	public ArrayList<HashMap<String, Integer>> getProfile() {
		return profile;
	}

	public void setProfile(ArrayList<HashMap<String, Integer>> profile) {
		this.profile = profile;
	}

	public ArrayList<String> getChiefsOfStaff() {
		return chiefsOfStaff;
	}

	public void setChiefsOfStaff(ArrayList<String> chiefsOfStaff) {
		this.chiefsOfStaff = chiefsOfStaff;
	}

}
