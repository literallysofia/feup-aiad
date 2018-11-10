package agents;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

import java.util.ArrayList;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import agentbehaviours.AnswerChiefOfStaff;
import agentbehaviours.ListeningCandidateBeliefs;
import agentbehaviours.ListeningChiefOfStaffQuestion;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;

public class Voter extends Agent {

	private String id;
	private String state;
	private int passivity;
	private int assertiveness;
	private int minCredibility;
	private HashMap<String, ArrayList<Integer>> beliefs = new HashMap<>();
	private int candidatesSize;
	private HashMap<String, HashMap<String, Integer>> candidatesBeliefs = new HashMap<>();
	private HashMap<String, Integer> candidatesCredibility = new HashMap<>();
	private HashMap<String, String> chiefOfStaffInfo = new HashMap<>();
	private String chosenCandidate;

	private DFAgentDescription dfd;

	public Voter(String id, String state, ArrayList<String> beliefs, int candidatesSize) {
		this.id = id;
		this.state = state;
		Random rnd = new Random();

		for (int i = 0; i < beliefs.size(); i++) {

			int first_value = rnd.nextInt(100) + 1;
			int second_value;

			if (first_value + 30 > 100)
				second_value = 100;
			else
				second_value = first_value + 30;

			ArrayList<Integer> range = new ArrayList<Integer>();
			range.add(first_value);
			range.add(second_value);

			this.beliefs.put(beliefs.get(i), range);
		}

		this.passivity = rnd.nextInt(100) + 1;
		this.assertiveness = rnd.nextInt(100) + 1;
		this.minCredibility = rnd.nextInt(100) + 1;
		this.setCandidatesSize(candidatesSize);
		System.out.println(
				" > VOTER: " + this.id + " BELIEFS: " + this.beliefs + " MIN CREDIBILITY: " + this.minCredibility);
		// + "\nPassivity: " + this.passivity
		// + " Assertiveness: " + this.assertiveness + "\nMin Credibility: " +
		// this.minCredibility + "\n");

	}

	public Map.Entry<String, Integer> calculateWrongBelief() {
		
		String candidate = null;
		while(candidate == null){
			candidate = this.chiefOfStaffInfo.get(this.chiefOfStaffInfo.keySet().toArray()[0]);
		}
		

		if (chosenCandidate == null || !candidate.equals(this.chosenCandidate)) {

			HashMap<String, Integer> beliefScores = new HashMap();

			HashMap<String, Integer> candidateBeliefs = new HashMap();
			candidateBeliefs = this.candidatesBeliefs.get(candidate);

			for (Map.Entry<String, ArrayList<Integer>> entry : this.beliefs.entrySet()) {
				String belief = entry.getKey();
				int first_value = entry.getValue().get(0);
				int second_value = entry.getValue().get(1);

				int median = ((second_value - first_value) / 2) + first_value;
				int score = Math.abs(median - candidateBeliefs.get(belief));

				beliefScores.put(belief, score);
				
			}
			
			Map.Entry<String, Integer> maxEntry = null;

			for (Map.Entry<String, Integer> entry : beliefScores.entrySet()) {
				if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
					maxEntry = entry;
				}
			}
			
			int first_value = this.beliefs.get(maxEntry.getKey()).get(0);
			int second_value = this.beliefs.get(maxEntry.getKey()).get(1);
			int median = ((second_value - first_value) / 2) + first_value;

			maxEntry.setValue(median);
			return maxEntry;

		} else {
			return null;
		}

	}

	public String getId() {
		return id;
	}

	public float getPassivity() {
		return passivity;
	}

	public void setPassivity(int passivity) {
		this.passivity = passivity;
	}

	public float getAssertiveness() {
		return assertiveness;
	}

	public void setAssertiveness(int assertiveness) {
		this.assertiveness = assertiveness;
	}

	public HashMap<String, ArrayList<Integer>> getBeliefs() {
		return beliefs;
	}

	public void setBeliefs(HashMap<String, ArrayList<Integer>> beliefs) {
		this.beliefs = beliefs;
	}

	public int getMinCredibility() {
		return minCredibility;
	}

	public void setMinCredibility(int minCredibility) {
		this.minCredibility = minCredibility;
	}

	public HashMap<String, HashMap<String, Integer>> getCandidatesBeliefs() {
		return candidatesBeliefs;
	}

	public void setCandidatesbeliefs(HashMap<String, HashMap<String, Integer>> candidatesBeliefs) {
		this.candidatesBeliefs = candidatesBeliefs;
	}

	public int getCandidatesSize() {
		return candidatesSize;
	}

	public void setCandidatesSize(int candidatesSize) {
		this.candidatesSize = candidatesSize;
	}

	public String getChosenCandidate() {
		return chosenCandidate;
	}

	public void setChosenCandidate(String chosenCandidate) {
		this.chosenCandidate = chosenCandidate;
	}

	public void setup() {
		register();
		addBehaviour(new ListeningChiefOfStaffQuestion(this));
		SequentialBehaviour chooseCandidateAndBeliefs = new SequentialBehaviour();
		chooseCandidateAndBeliefs.addSubBehaviour(new ListeningCandidateBeliefs(this));
		chooseCandidateAndBeliefs.addSubBehaviour(new AnswerChiefOfStaff(this));
		addBehaviour(chooseCandidateAndBeliefs);
	}

	public void register() {
		ServiceDescription sd = new ServiceDescription();
		sd.setType(this.state);
		sd.setName(getLocalName());

		this.dfd = new DFAgentDescription();
		dfd.setName(getAID());
		dfd.addServices(sd);
		try {
			DFService.register(this, this.dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}

	public void takeDown() {
		System.out.println(getLocalName() + ": You won, Frank.");
	}


	public void chooseCandidate() {

		// System.out.println(" - VOTER: " + this.getLocalName() + " CANDIDATES
		// BELIEFS: " + this.candidatesBeliefs);

		ArrayList<String> possibleCandidates = new ArrayList<String>();
		int minBeliefs = (int) Math.ceil(beliefs.size() / 2.0);

		for (Map.Entry<String, HashMap<String, Integer>> entry : this.candidatesBeliefs.entrySet()) {
			String candidate = entry.getKey();
			HashMap<String, Integer> candidatebeliefs = entry.getValue();

			int wrongBeliefs = 0;

			for (Map.Entry<String, Integer> entry2 : candidatebeliefs.entrySet()) {
				String belief = entry2.getKey();
				Integer value = entry2.getValue();

				if (value < this.beliefs.get(belief).get(0) || this.beliefs.get(belief).get(1) < value)
					wrongBeliefs++;

			}
			// System.out.println("VOTER: " + this.getLocalName() + " CANDIDATE:
			// " + candidate + " WRONG BELIIEFS: " + wrongBeliefs);
			if (wrongBeliefs <= minBeliefs && this.candidatesCredibility.get(candidate) > this.minCredibility)
				possibleCandidates.add(candidate);

		}

		// System.out.println(" - VOTER: " + this.getLocalName() + " POSSIBLE
		// CANDIDATES: " + possibleCandidates);

		if (possibleCandidates.size() != 0) {
			Random rnd = new Random();
			int chosenCandidateIndex = rnd.nextInt(possibleCandidates.size());
			this.chosenCandidate = possibleCandidates.get(chosenCandidateIndex);
		} else {
			this.chosenCandidate = null;
		}

		System.out.println("         - VOTER: " + this.getLocalName() + " CHOSEN CANDIDATE: " + this.chosenCandidate);

	}

	public HashMap<String, Integer> getCandidatesCredibility() {
		return candidatesCredibility;
	}

	public void setCandidatesCredibility(HashMap<String, Integer> candidatesCredibility) {
		this.candidatesCredibility = candidatesCredibility;
	}

	public HashMap<String, String> getChiefOfStaffInfo() {
		return chiefOfStaffInfo;
	}

	public void setChiefOfStaffInfo(HashMap<String, String> chiefOfStaffInfo) {
		this.chiefOfStaffInfo = chiefOfStaffInfo;
	}

}
