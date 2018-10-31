package agents;

import java.util.HashMap;
import java.util.ArrayList;

import jade.core.Agent;
import jade.core.behaviours.*;
import agentbehaviours.Asking;
import agentbehaviours.Intimidate;

public class Voter extends Agent{
	
	int id;
	private float passivity;
	private float assertiveness;
	private int minCredibility;
	private HashMap<String, ArrayList<Float>> beliefs = new HashMap<>(); 
	
	public Voter() {
		
	}
	
	public Voter(float passivity, float assertiveness, HashMap<String, ArrayList<Float>> beliefs) {
		this.passivity = passivity;
		this.assertiveness = assertiveness;
		this.beliefs = beliefs;
	}
	
	public int getId(){
		return id;
	}
	
	public float getPassivity() {
		return passivity;
	}

	public void setPassivity(float passivity) {
		this.passivity = passivity;
	}

	public float getAssertiveness() {
		return assertiveness;
	}

	public void setAssertiveness(float assertiveness) {
		this.assertiveness = assertiveness;
	}

	public HashMap<String, ArrayList<Float>> getBeliefs() {
		return beliefs;
	}

	public void setBeliefs(HashMap<String, ArrayList<Float>> beliefs) {
		this.beliefs = beliefs;
	}
	
	public void setup(){
        addBehaviour(new Asking(this));
    }
	
	public void takeDown(){
        System.out.println(getLocalName()+ ": You won, Frank.");
    }

	public int getMinCredibility() {
		return minCredibility;
	}

	public void setMinCredibility(int minCredibility) {
		this.minCredibility = minCredibility;
	}

}
