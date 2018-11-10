package agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

import agentbehaviours.ChiefOfStaffListeningVoter;
import agentbehaviours.ListeningChiefOfStaffQuestion;
import agentbehaviours.SayWhatToChange;
import agentbehaviours.SendQuestion;
import agentbehaviours.WhatToChange;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ChiefOfStaff extends Agent {

	private Candidate boss;
	private String state;
	private ArrayList<String> stateChosenCandidates = new ArrayList();
	private HashMap<String, ArrayList<Integer>> stateChosenBeliefs = new HashMap<>();
	private String chosenCandidate;
	private String chosenBelief;
	private int chosenValue;
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

	public HashMap<String, ArrayList<Integer>> getStateChosenBeliefs() {
		return stateChosenBeliefs;
	}

	public void setStateChosenBeliefs(HashMap<String, ArrayList<Integer>> stateChosenBeliefs) {
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
		talkWithVoter.addSubBehaviour(new SendQuestion(this));
		talkWithVoter.addSubBehaviour(new ChiefOfStaffListeningVoter(this));
		talkWithVoter.addSubBehaviour(new SayWhatToChange(this, MessageTemplate.MatchPerformative(ACLMessage.CFP)));
		addBehaviour(talkWithVoter);
	}

	public void calculateChooseCandidate() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (int i = 0; i < this.stateChosenCandidates.size(); i++) {
			Integer count = map.get(stateChosenCandidates.get(i));
			map.put(stateChosenCandidates.get(i), count == null ? 1 : count + 1); // count
		}

		Map.Entry<String, Integer> maxEntry = null;
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
				maxEntry = entry;
			}
		}

		/*System.out.println("CANDIDATES: " + map);
		System.out.println("CANDIDATE: " + maxEntry.getKey());*/
		this.chosenCandidate = maxEntry.getKey();
	}

	public void calculateChooseBelief() {
		Map<String, Integer> map2 = new HashMap<String, Integer>();
		List<String> keys = new ArrayList<String>(this.stateChosenBeliefs.keySet());

		for (int i = 0; i < keys.size(); i++) {
			Integer count = this.stateChosenBeliefs.get(keys.get(i)).size();
			map2.put(keys.get(i), count);
		}

		Map.Entry<String, Integer> maxEntry2 = null;
		for (Map.Entry<String, Integer> entry : map2.entrySet()) {
			if (maxEntry2 == null || entry.getValue().compareTo(maxEntry2.getValue()) > 0) {
				maxEntry2 = entry;
			}
			else if(entry.getValue().compareTo(maxEntry2.getValue()) == 0 
					&& maxEntry2.getKey() == null){
				maxEntry2 = entry;
			}
		}

		ArrayList<Integer> values = this.stateChosenBeliefs.get(maxEntry2.getKey());
		int average = (int) calculateAverage(values);

		/*System.out.println("BELIEFS: " + map2);
		System.out.println("BELIEF: " + maxEntry2.getKey());
		System.out.println("VALUES: " + values);
		System.out.println("VALUE: " + average);*/
		
		this.chosenBelief = maxEntry2.getKey();
		this.chosenValue = average;
	}

	private double calculateAverage(List<Integer> marks) {
		Integer sum = 0;
		if (!marks.isEmpty()) {
			for (Integer mark : marks) {
				sum += mark;
			}
			return sum.doubleValue() / marks.size();
		}
		return sum;
	}

	public int getNrVotersState() {
		return nrVotersState;
	}

	public void setNrVotersState(int nrVotersState) {
		this.nrVotersState = nrVotersState;
	}

	public int getChosenValue() {
		return chosenValue;
	}

	public void setChosenValue(int chosenValue) {
		this.chosenValue = chosenValue;
	}

}
