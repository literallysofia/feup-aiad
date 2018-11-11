package agents;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import agentbehaviours.ChiefListenVoterChoices;
import agentbehaviours.ChiefSendCandidateStatus;
import agentbehaviours.ChiefSendVoterQuestion;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ChiefOfStaff extends Agent {

	public Logger logger;
	private String id;
	private Candidate boss;
	private String state;
	private ArrayList<String> stateChosenCandidates = new ArrayList<>();
	private HashMap<String, ArrayList<Integer>> stateChosenBeliefs = new HashMap<>();
	private String chosenCandidate;
	private String chosenBelief;
	private int chosenValue;
	private int nrVotersState;

	public ChiefOfStaff(String id, Candidate candidate, String state) {
		this.id = id;
		this.boss = candidate;
		this.state = state;

		setupLogger();
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

	public void setupLogger() {

		this.logger = Logger.getLogger(this.id);
		FileHandler fh = null;
		this.logger.setUseParentHandlers(false);

		try {
			File logDir = new File("logs/");
			if (!(logDir.exists()))
				logDir.mkdir();

			fh = new FileHandler("logs/" + this.id + ".log");
			this.logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void setup() {
		/*System.out.println(
				" > CHIEF: " + this.getLocalName() + " STATE: " + this.state + " BOSS: " + this.boss.getLocalName());*/
		this.logger.info("> INFO:    ID: " +  this.getLocalName() + " STATE: " + this.state + " BOSS: " + this.boss.getLocalName());
		System.out.println("> INFO:    ID: " +  this.getLocalName() + " STATE: " + this.state + " BOSS: " + this.boss.getLocalName());
		SequentialBehaviour talkWithVoter = new SequentialBehaviour();
		talkWithVoter.addSubBehaviour(new ChiefSendVoterQuestion(this));
		talkWithVoter.addSubBehaviour(new ChiefListenVoterChoices(this));
		talkWithVoter
				.addSubBehaviour(new ChiefSendCandidateStatus(this, MessageTemplate.MatchPerformative(ACLMessage.CFP)));
		addBehaviour(talkWithVoter);
	}

	// calcula o candidato escolhido pelos voters do seu estado
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

		this.chosenCandidate = maxEntry.getKey();
	}

	// calcula a belief escolhido pelos voters do seu estado (belief do seu
	// candidato que esses voters acham que está mais aquém das suas
	// expectativas)
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
			} else if (entry.getValue().compareTo(maxEntry2.getValue()) == 0 && maxEntry2.getKey() == null) {
				maxEntry2 = entry;
			}
		}

		ArrayList<Integer> values = this.stateChosenBeliefs.get(maxEntry2.getKey());
		int average = (int) calculateAverage(values);

		this.chosenBelief = maxEntry2.getKey();
		this.chosenValue = average;
	}

	// calcula a média de um arraylist de ineteiros
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

}
