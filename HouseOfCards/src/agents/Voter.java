package agents;

import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;

import jade.core.Agent;
import jade.core.behaviours.*;
import agentbehaviours.Asking;

public class Voter extends Agent {

	private String id;
	private float passivity;
	private float assertiveness;
	private int minCredibility;
	private HashMap<String, ArrayList<Integer>> beliefs = new HashMap<>();

	public Voter(String id, ArrayList<String> beliefs) {
		this.id = id;

		for (int i = 0; i < beliefs.size(); i++) {
			Random rnd = new Random();
			int first_value = rnd.nextInt(100) + 1;
			int second_value = rnd.nextInt(100 - first_value + 1) + first_value;
			
			ArrayList<Integer> range = new ArrayList<Integer>();
			range.add(first_value);
			range.add(second_value);
			
			this.beliefs.put(beliefs.get(i), range);
		}
		System.out.println(this.id + "\nBeliefs: " + this.beliefs);
	}

	public Voter(float passivity, float assertiveness, HashMap<String, ArrayList<Integer>> beliefs) {
		this.passivity = passivity;
		this.assertiveness = assertiveness;
		this.beliefs = beliefs;
	}

	public String getId() {
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

	public HashMap<String, ArrayList<Integer>> getBeliefs() {
		return beliefs;
	}

	public void setBeliefs(HashMap<String, ArrayList<Integer>> beliefs) {
		this.beliefs = beliefs;
	}

	public void setup() {
		addBehaviour(new Asking(this));
	}

	public void takeDown() {
		System.out.println(getLocalName() + ": You won, Frank.");
	}

	public int getMinCredibility() {
		return minCredibility;
	}

	public void setMinCredibility(int minCredibility) {
		this.minCredibility = minCredibility;
	}

}
