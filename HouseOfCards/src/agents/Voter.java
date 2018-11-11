package agents;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import agentbehaviours.VoterSendChiefChoices;
import agentbehaviours.CandidateListenChiefIsFinished;
import agentbehaviours.CandidateListenChiefStatus;
import agentbehaviours.CandidateSendBeliefs;
import agentbehaviours.VoterListenCandidateAndChief;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.lang.acl.ACLMessage;
import jade.domain.FIPAException;

public class Voter extends Agent {
	public Logger logger;
	private String id;
	private String state;
	private int minCredibility;
	private HashMap<String, ArrayList<Integer>> beliefs = new HashMap<>();
	private int candidatesSize;
	private HashMap<String, HashMap<String, Integer>> candidatesBeliefs = new HashMap<>();
	private HashMap<String, Integer> candidatesCredibility = new HashMap<>();
	private HashMap<String, String> chiefOfStaffInfo = new HashMap<>();
	private String chosenCandidate;
	private boolean readyToVote = false;

	private DFAgentDescription dfd;

	public Voter(String id, String state, ArrayList<String> beliefs, int candidatesSize) {
		this.id = id;
		this.state = state;
		Random rnd = new Random();

		for (int i = 0; i < beliefs.size(); i++) {

			int first_value = rnd.nextInt(100) + 1;
			int second_value;

			if (first_value + 50 > 100)
				second_value = 100;
			else
				second_value = first_value + 50;

			ArrayList<Integer> range = new ArrayList<Integer>();
			range.add(first_value);
			range.add(second_value);

			this.beliefs.put(beliefs.get(i), range);
		}

		this.minCredibility = rnd.nextInt(100) + 1;
		this.setCandidatesSize(candidatesSize);

		setupLogger();

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
	
	public boolean isReadyToVote() {
		return readyToVote;
	}

	public void setReadyToVote(boolean readyToVote) {
		this.readyToVote = readyToVote;
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
		register();	
		SequentialBehaviour loop = new SequentialBehaviour();
				loop.addSubBehaviour(new VoterListenCandidateAndChief(this,1));
				loop.addSubBehaviour(new VoterSendChiefChoices(this));
		addBehaviour(loop);
	}
	
	public void takeDown() {
		System.out.println(this.getLocalName() + " was taken down.");
	}

	// regista nas paginas amarelas
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

	// escolhe o candidato de acordo com as suas beliefs
	public void chooseCandidate() {

		ArrayList<String> possibleCandidates = new ArrayList<String>();
		int maxWrongBeliefs = (int) Math.ceil(beliefs.size() / 2.0); // maximo
																		// de
																		// beliefs
																		// que o
																		// candidato
																		// pode
																		// ter
																		// que
																		// nao
																		// estão
																		// dentro
																		// dos
																		// intervalos
																		// do
																		// voter

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

			if (wrongBeliefs <= maxWrongBeliefs && this.candidatesCredibility.get(candidate) > this.minCredibility)
				possibleCandidates.add(candidate);

		}

		if (possibleCandidates.size() != 0) {
			Random rnd = new Random();
			int chosenCandidateIndex = rnd.nextInt(possibleCandidates.size());
			this.chosenCandidate = possibleCandidates.get(chosenCandidateIndex);
		} else {
			this.chosenCandidate = null;
		}
		
		this.logger.info("> INFO:    CHOSEN CANDIDATE: " + this.chosenCandidate);
		System.out.println("> INFO:    ID: " + this.getLocalName() + " CHOSEN CANDIDATE: " + this.chosenCandidate);

	}

	// calcula a belief mais aquem do candidato do chief of stuff
	public Map.Entry<String, Integer> calculateWrongBelief() {

		String candidate = null;
		while (this.chiefOfStaffInfo.keySet().size() == 0 && candidate == null) {

		}

		candidate = this.chiefOfStaffInfo.get(this.chiefOfStaffInfo.keySet().toArray()[0]);

		if (chosenCandidate == null || !candidate.equals(this.chosenCandidate)) {

			HashMap<String, Integer> beliefScores = new HashMap<>();

			HashMap<String, Integer> candidateBeliefs = new HashMap<>();
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

}
