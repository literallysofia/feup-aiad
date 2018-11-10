package agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import agentbehaviours.ChiefOfStaffListeningVoter;
import agentbehaviours.ListeningChiefOfStaffQuestion;
import agentbehaviours.SayWhatToChange;
import agentbehaviours.SendQuestion;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ChiefOfStaff extends Agent {

	private Candidate boss;
	private String state;
	private ArrayList<String> stateChosenCandidates = new ArrayList();
	private ArrayList<String> stateChosenBeliefs = new ArrayList();
	private String chosenCandidate;
	private String chosenBelief;
	private int nrVotersState;

	public ChiefOfStaff(Candidate candidate, String state) {
		this.boss = candidate;
		this.state = state;
	}

	public Candidate getBoss() {
		return boss;
	}

	public String getStateName() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setBoss(Candidate boss) {
		this.boss = boss;
	}

	public ArrayList<String> getStateChosenCandidates() {
		return stateChosenCandidates;
	}

	public void setStateChosenCandidates(ArrayList<String> stateChosenCandidates) {
		this.stateChosenCandidates = stateChosenCandidates;
	}

	public ArrayList<String> getStateChosenBeliefs() {
		return stateChosenBeliefs;
	}

	public void setStateChosenBeliefs(ArrayList<String> stateChosenBeliefs) {
		this.stateChosenBeliefs = stateChosenBeliefs;
	}

	public String getChosenCandidate() {
		return chosenCandidate;
	}

	public void setChosenCandidate(String chosenCandidate) {
		this.chosenCandidate = chosenCandidate;
	}

	public String getChosenBelief() {
		return chosenBelief;
	}

	public void setChosenBelief(String chosenBelief) {
		this.chosenBelief = chosenBelief;
	}

	public void setup() {
		System.out
				.println(" > CHIEF: " + this.getLocalName() + " STATE: " + this.state + " BOSS: " + this.boss.getId());
		SequentialBehaviour talkWithVoter = new SequentialBehaviour();
		addBehaviour(new SendQuestion(this));
		addBehaviour(new ChiefOfStaffListeningVoter(this));
	}

	public void calculateChosens() {

		//CHOOSE CANDIDATE
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (int i = 0; i < this.stateChosenCandidates.size(); i++) {
			Integer count = map.get(stateChosenCandidates.get(i));
			map.put(stateChosenCandidates.get(i), count == null ? 1 : count + 1);																	// count
		}

		Map.Entry<String, Integer> maxEntry = null;
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
				maxEntry = entry;
			}
		}


		System.out.println("CANDIDATES: " + map);
		System.out.println("CANDIDATE: " + maxEntry.getKey());
		this.chosenCandidate = maxEntry.getKey();

		//CHOOSE BELIEF
		Map<String, Integer> map2 = new HashMap<String, Integer>();
		for (int i = 0; i < this.stateChosenBeliefs.size(); i++) {
			Integer count = map2.get(stateChosenBeliefs.get(i));
			map2.put(stateChosenBeliefs.get(i), count == null ? 1 : count + 1);
		}

		Map.Entry<String, Integer> maxEntry2 = null;
		for (Map.Entry<String, Integer> entry : map2.entrySet()) {
			if (maxEntry2 == null || entry.getValue().compareTo(maxEntry2.getValue()) > 0) {
				maxEntry2 = entry;
			}
		}
		System.out.println("BELIEFS: " + map2);
		System.out.println("BELIEF: " + maxEntry2.getKey());
		this.chosenBelief = maxEntry2.getKey();

	}

	public int getNrVotersState() {
		return nrVotersState;
	}

	public void setNrVotersState(int nrVotersState) {
		this.nrVotersState = nrVotersState;
	}

}
