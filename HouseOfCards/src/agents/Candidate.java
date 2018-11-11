package agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import agentbehaviours.CandidateListeningChiefIsFinished;
import agentbehaviours.CandidateSendBeliefs;
import agentbehaviours.CandidateListenCheidStatus;

public class Candidate extends Agent {

	private String id;
	private int credibility = 100;
	private ArrayList<String> states = new ArrayList<String>();
	private HashMap<String, Integer> beliefs = new HashMap<>();
	private ArrayList<String> chiefsOfStaff = new ArrayList<>();
	private HashMap<String, Integer> beliefToChangePopulation = new HashMap<>();
	private HashMap<String, Integer> beliefToChangeValue = new HashMap<>();

	public Candidate(String id, ArrayList<String> states, ArrayList<String> beliefs) {
		this.id = id;

		this.states = states;

		for (int i = 0; i < beliefs.size(); i++) {
			Random rnd = new Random();
			int value = rnd.nextInt(100) + 1;
			this.beliefs.put(beliefs.get(i), value);
		}
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

	public ArrayList<String> getChiefsOfStaff() {
		return chiefsOfStaff;
	}

	public void setChiefsOfStaff(ArrayList<String> chiefsOfStaff) {
		this.chiefsOfStaff = chiefsOfStaff;
	}

	public int getCredibility() {
		return credibility;
	}

	public void setCredibility(int credibility) {
		this.credibility = credibility;
	}

	public HashMap<String, Integer> getBeliefToChangePopulation() {
		return beliefToChangePopulation;
	}

	public void setBeliefToChangePopulation(HashMap<String, Integer> beliefToChangePopulation) {
		this.beliefToChangePopulation = beliefToChangePopulation;
	}

	public HashMap<String, Integer> getBeliefToChangeValue() {
		return beliefToChangeValue;
	}

	public void setBeliefToChangeValue(HashMap<String, Integer> beliefToChangeValue) {
		this.beliefToChangeValue = beliefToChangeValue;
	}

	public void setup() {
		System.out.println(" > CANDIDATE: " + this.getLocalName() + " BELIEFS: " + this.beliefs);
		addBehaviour(new CandidateSendBeliefs(this));
		SequentialBehaviour trial = new SequentialBehaviour();
		trial.addSubBehaviour(new CandidateListeningChiefIsFinished(this));
		trial.addSubBehaviour(new CandidateListenCheidStatus(this, new ACLMessage(ACLMessage.CFP)));
		addBehaviour(trial);
	}

	// atualiza as beliefs conforme a informa��o dada pelo chief of staff
	public void changeBeliefs() {

		if (this.beliefToChangePopulation.size() != 0 && this.beliefToChangeValue.size() != 0) {
			Map.Entry<String, Integer> maxEntry = null; // belief com mais
														// popula��o
			for (Map.Entry<String, Integer> entry : this.beliefToChangePopulation.entrySet()) {
				if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
					maxEntry = entry;
				}
			}
			// System.out.println("BELIEFS_POP: " +
			// this.beliefToChangePopulation);
			// System.out.println("BELIEFS_VALUE: " + this.beliefToChangeValue);

			// System.out.println("OLD BELIEFS: " + this.beliefs);
			int value = this.beliefToChangeValue.get(maxEntry.getKey());
			int oldValue = this.beliefs.get(maxEntry.getKey());
			this.beliefs.replace(maxEntry.getKey(), value);

			// System.out.println("NEW BELIEFS: " + this.beliefs);
			// System.out.println("OLD CREDIBILITY: " + this.credibility);

			int diff = Math.abs(oldValue - value);
			// System.out.println("DIFF BELIEF: " + diff);

			int diffCre = (int) Math.ceil(diff / 4.0); // credibilidade perde um
														// quarto do valor da
														// mudan�a da cren�a
			// System.out.println("DIFF CRED: " + diffCre);

			this.credibility = this.credibility - diffCre;
			// System.out.println("NEW CREDIBILITY: " + this.credibility);

			System.out.println("                           - CANDIDATE: " + this.getLocalName() + " CHANGED BELIEF: "
					+ maxEntry.getKey() + " OLD VALUE: " + oldValue + " NEW VALUE : " + value + " CHANGED CREDIBILITY: "
					+ this.credibility);

		}
	}

}
