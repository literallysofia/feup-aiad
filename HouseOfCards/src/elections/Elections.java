package elections;

import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;
import java.util.Random;

import agents.Candidate;
import agents.ChiefOfStaff;
import agents.Voter;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;


public class Elections {

	private int minPopulation, maxPopulation, nrCandidates;
	private ArrayList<String> states = new ArrayList<String>();
	private ArrayList<String> beliefs = new ArrayList<String>();
	
	private Runtime rt;
	private Profile p;
	private ContainerController cc;
	
	

	public Elections(int minPopulation, int maxPopulation, int nrCandidates) throws StaleProxyException {

		this.minPopulation = minPopulation;
		this.maxPopulation = maxPopulation;
		this.nrCandidates = nrCandidates;

		this.states.add("Alaska");
		this.states.add("California");
		// this.states.add("Florida");
		// this.states.add("Hawaii");
		// this.states.add("Kansas");
		// this.states.add("Montana");
		// this.states.add("New Jersey");
		// this.states.add("New York");
		// this.states.add("Washington");
		// this.states.add("Texas");

		this.beliefs.add("Liberalism");
		this.beliefs.add("Conservatism");
		this.beliefs.add("Communism");
		this.beliefs.add("Socialism");
		//this.beliefs.add("Anarchism");
		// this.beliefs.add("Nationalism");
		// this.beliefs.add("Fascism");
		// this.beliefs.add("Monarchism");
		// this.beliefs.add("Republicanism");
		// this.beliefs.add("Environmentalism");

		this.rt = Runtime.instance();
		this.p = new ProfileImpl(true);
		this.cc = rt.createMainContainer(p);
		

		try {
			createVotersPerState();
			createCandidatesAndChiefs();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		

	}

	public int getMinPopulation() {
		return this.minPopulation;
	}

	public int getMaxPopulation() {
		return this.maxPopulation;
	}

	public ArrayList<String> getStates() {
		return states;
	}

	public void setStates(ArrayList<String> states) {
		this.states = states;
	}

	public ArrayList<String> getBeliefs() {
		return beliefs;
	}

	public void setBeliefs(ArrayList<String> beliefs) {
		this.beliefs = beliefs;
	}

	// min_state_population, max_state_population, nr_candidates
	public static void main(String args[]) throws StaleProxyException {
		Elections elections = new Elections(Integer.parseInt(args[0]), Integer.parseInt(args[1]),
				Integer.parseInt(args[2]));
	}

	public void createVotersPerState() throws StaleProxyException {

		for (int id_state = 0; id_state < this.states.size(); id_state++) {
			int id_voterstate = 0;

			Random rnd = new Random();
			int population = rnd.nextInt(this.maxPopulation + 1 - this.minPopulation) + this.minPopulation;
			System.out.println(" > STATE: " + this.states.get(id_state) + " " + " POPULATION: " + population);

			while (id_voterstate < population) {
				try {
					String id = "voter_" + this.states.get(id_state) + "_" + Integer.toString(id_voterstate);
					AgentController ac = this.cc.acceptNewAgent(id, new Voter(id, this.states.get(id_state),this.beliefs, this.nrCandidates));
					ac.start();
					id_voterstate++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	
		
	}

	public void createCandidatesAndChiefs() throws StaleProxyException {
		
		ArrayList<Candidate> candidates = new ArrayList<Candidate>();


		System.out.println(" > CANDIDATES: " + this.nrCandidates);

		for (int id_candidate = 0; id_candidate < this.nrCandidates; id_candidate++) {
			String id = "candidate_" + Integer.toString(id_candidate);
			Candidate candidate = new Candidate(id,this.states, this.beliefs);
			AgentController ac = this.cc.acceptNewAgent(id, candidate);
			ac.start();
			candidates.add(candidate);
		}

		for (int id_chief = 0; id_chief < this.states.size(); id_chief++) {
			Random rnd = new Random();
			int candidateIndex = rnd.nextInt(candidates.size());
			
			
			String id = "chiefofstaff_" + Integer.toString(id_chief);
			AgentController ac = this.cc.acceptNewAgent(id, new ChiefOfStaff(candidates.get(candidateIndex), this.states.get(id_chief)));
			candidates.get(candidateIndex).getChiefsOfStaff().add(id);
			ac.start();
		}

	}

}
