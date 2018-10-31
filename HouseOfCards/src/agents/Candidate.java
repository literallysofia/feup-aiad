package agents;

import java.util.ArrayList;
import java.util.HashMap;

import jade.core.Agent;
import jade.core.behaviours.*;
import agentbehaviours.Asking;

public class Candidate extends Agent{

	private int id;
	private int credibility=100; 
	private HashMap<String, Float> beliefs = new HashMap<>(); 
	
	public Candidate() {
		// TODO Auto-generated constructor stub
	}
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
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
	
	public void setup(){
        addBehaviour(new Asking(this));
    }

}
