package agents;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import agentbehaviours.ListeningCandidateBeliefs;
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

	private DFAgentDescription dfd;

	public Voter(String id, String state, ArrayList<String> beliefs, int candidatesSize) {
		this.id = id;
		this.state = state;
		Random rnd = new Random();

		for (int i = 0; i < beliefs.size(); i++) {

			int first_value = rnd.nextInt(100) + 1;
			int second_value = rnd.nextInt(100 - first_value + 1) + first_value;

			ArrayList<Integer> range = new ArrayList<Integer>();
			range.add(first_value);
			range.add(second_value);

			this.beliefs.put(beliefs.get(i), range);
		}

		this.passivity = rnd.nextInt(100) + 1;
		this.assertiveness = rnd.nextInt(100) + 1;
		this.minCredibility = rnd.nextInt(100) + 1;
		this.setCandidatesSize(candidatesSize);
		System.out.println(" > VOTER: " + this.id + " BELIEFS: " + this.beliefs);
		// + "\nPassivity: " + this.passivity
		// + " Assertiveness: " + this.assertiveness + "\nMin Credibility: " +
		// this.minCredibility + "\n");

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

	public void setup() {
		register();
		addBehaviour(new ListeningCandidateBeliefs(this));
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

	// TODO: Comentar codigo
	public void chooseCandidate() {
		System.out.println("      - VOTER: " + this.getLocalName() + " CANDIDATES BELIEFS: " + this.candidatesBeliefs);

		String chosenCandidate = null;
		int difference = 0;

		int minBeliefs = (int) Math.ceil(beliefs.size() / 2);

		for (Map.Entry<String, HashMap<String, Integer>> entry : this.candidatesBeliefs.entrySet()) {
			String candidate = entry.getKey();
			HashMap<String, Integer> candidatebeliefs = entry.getValue();

			int wrongBeliefs = 0;

			for (Map.Entry<String, Integer> entry2 : candidatebeliefs.entrySet()) {

				if (wrongBeliefs < minBeliefs) {
					String belief = entry2.getKey();
					Integer value = entry2.getValue();

					if (value < this.beliefs.get(belief).get(0) || this.beliefs.get(belief).get(1) < value) {
						wrongBeliefs++;
					} else {
						// calcular o value do candidato
					}
				} else {
					break;
				}
			}
			
		}
		
	}
	
}
