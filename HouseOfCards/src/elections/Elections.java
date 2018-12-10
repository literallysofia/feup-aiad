package elections;

import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import agents.Candidate;
import agents.ChiefOfStaff;
import agents.State;
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
	private HashMap<String, State> statesObjects =  new HashMap();
	private ArrayList<String> beliefs = new ArrayList<String>();
	private ArrayList<Voter> voters = new ArrayList();
	private ArrayList<Candidate> candidates = new ArrayList();
	private Runtime rt;
	private Profile p;
	private ContainerController cc;

	public Elections(int minPopulation, int maxPopulation, int nrCandidates) throws StaleProxyException {

		this.minPopulation = minPopulation;
		this.maxPopulation = maxPopulation;
		this.nrCandidates = nrCandidates;

		this.states.add("Alaska");
		this.states.add("California");
		this.states.add("Florida");
		this.states.add("Hawaii");
		this.states.add("Kansas");
		//this.states.add("Montana");
		//this.states.add("New Jersey");
		//this.states.add("New York");
		//this.states.add("Washington");
		//this.states.add("Texas");
		
		State state;
		for(int i=0; i < this.states.size(); i++){
			state = new State (this.states.get(i));
			this.statesObjects.put(this.states.get(i), state);
		}

		this.beliefs.add("Liberalism");
		this.beliefs.add("Conservatism");
		this.beliefs.add("Communism");
		this.beliefs.add("Socialism");
		this.beliefs.add("Anarchism");
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
	public static void main(String args[]) throws StaleProxyException, InterruptedException {
		int x=1;
		Elections elections;
		while(x<=200){
			System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS] %5$s%6$s%n");
			System.out.println("TIME: " +  x);
			elections = new Elections(Integer.parseInt(args[0]), Integer.parseInt(args[1]),Integer.parseInt(args[2]));
			elections.getVotes();
			elections.cc.kill();
			elections.rt.shutDown();
			//Thread.sleep(3000);
			x++;
		}
		System.exit(0);
	}

	
	public void createVotersPerState() throws StaleProxyException {

		int total = 0;

		for (int id_state = 0; id_state < this.states.size(); id_state++) {
			int id_voterstate = 0;

			Random rnd = new Random();
			int population = rnd.nextInt(this.maxPopulation + 1 - this.minPopulation) + this.minPopulation;
			System.out.println("> STATE: " + this.states.get(id_state) + " " + " POPULATION: " + population);
			total = total + population;
			while (id_voterstate < population) {
				try {
					String id = "voter_" + this.states.get(id_state) + "_" + Integer.toString(id_voterstate);
					Voter voter = new Voter(id, this.states.get(id_state), this.beliefs, this.nrCandidates);
					voters.add(voter);
					AgentController ac = this.cc.acceptNewAgent(id, voter);
					ac.start();
					id_voterstate++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println("> TOTAL POPULATION: " + total);
	}

	public void createCandidatesAndChiefs() throws StaleProxyException {

		ArrayList<Candidate> candidates = new ArrayList<Candidate>();
		System.out.println("> NR CANDIDATES: " + this.nrCandidates);

		for (int id_candidate = 0; id_candidate < this.nrCandidates; id_candidate++) {
			String id = "candidate_" + Integer.toString(id_candidate);
			Candidate candidate = new Candidate(id, this.states, this.beliefs);
			AgentController ac = this.cc.acceptNewAgent(id, candidate);
			ac.start();
			candidates.add(candidate);
			this.candidates.add(candidate);
		}

		for (int id_chief = 0; id_chief < this.states.size(); id_chief++) {
			Random rnd = new Random();
			int candidateIndex = rnd.nextInt(candidates.size());

			String id = "chiefofstaff_" + Integer.toString(id_chief);
			AgentController ac = this.cc.acceptNewAgent(id,
					new ChiefOfStaff(id, candidates.get(candidateIndex), this.states.get(id_chief)));
			candidates.get(candidateIndex).getChiefsOfStaff().add(id);
			ac.start();
		}

	}

	public void getVotes() {
		
		HashMap<String, String> votes = new HashMap<>();
		
		while(votes.size()<voters.size()){
			for(int i = 0; i < voters.size(); i++){
				if(voters.get(i).isReadyToVote()){
					votes.put(voters.get(i).getLocalName(), voters.get(i).getChosenCandidate());
				}
			}
		}
		
		System.out.println("> VOTES: " + votes);
		
		Map<String, Integer> counts = new HashMap<>();
		for (String c : votes.values()) {
		    int value = counts.get(c) == null ? 0 : counts.get(c);
		    counts.put(c, value + 1);
		}
		
		
		Map.Entry<String, Integer> winner = null;
		for (Map.Entry<String, Integer> entry : counts.entrySet())
		{
		    if (winner == null || entry.getValue().compareTo(winner.getValue()) > 0)
		        winner = entry;
		}
		
		//System.out.println(counts);
		System.out.println("> WINNER: " + winner.getKey() + " VOTES: " + winner.getValue());
		
		try {
			dataAnalysisLogger(winner.getKey());
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	public void fillStatesObjects(){
		for(int i=0; i < this.voters.size(); i++){
			State state = this.statesObjects.get(this.voters.get(i).getStateElection());
			String candidate = this.voters.get(i).getChosenCandidate();
			state.addVote(candidate);
			state.addBeliefs(this.voters.get(i).getBeliefs());
		}
		
		for (Map.Entry<String, State> entry : this.statesObjects.entrySet()) {
		    entry.getValue().calculateWinner();
		    entry.getValue().calculateBelief();
		}
	}
	
	
	public void dataAnalysisLogger(String winner) throws IOException{
		fillStatesObjects();
		
		File file= new File ("logs/rapidData.csv");
		FileWriter fw;
		if (file.exists()){
			fw = new FileWriter(file,true);
		}
		else{
			file.createNewFile();
			fw = new FileWriter(file);
		}
		
	    PrintWriter printWriter = new PrintWriter(fw);
	    
	    for(int i=0; i<this.candidates.size(); i++){
	    	String candidateId = this.candidates.get(i).getLocalName();
	    	int credibility = this.candidates.get(i).getCredibility();
	    	int stubbornness =  this.candidates.get(i).getStubbornness();
	    	
	    	boolean won = false;
	    	if(this.candidates.get(i).getLocalName().equals(winner))
	    		won = true;
	    	
	    	Map.Entry<String, Integer> maxBelief = null;
			for (Map.Entry<String, Integer> entry : this.candidates.get(i).getBeliefs().entrySet())
			{
			    if (maxBelief == null || entry.getValue().compareTo(maxBelief.getValue()) > 0)
			        maxBelief = entry;
			}
			String mainCandidateBelief =  maxBelief.getKey();
			
			int nChiefs = this.candidates.get(i).getChiefsOfStaff().size();
	    	int candidatePopulation = 0;
			for(int j=0; j<this.states.size();j++){
	    		String[] chiefsStatesArray = this.candidates.get(i).getChiefsStates().toArray(new String[this.candidates.get(i).getChiefsStates().size()]);
	    		for(int k=0; k < chiefsStatesArray.length; k++){
	   			String ChiefsState = chiefsStatesArray[k];    			
	    			if(ChiefsState.equals(this.states.get(j))){
	    				candidatePopulation = candidatePopulation + this.statesObjects.get(this.states.get(j)).getPopulation();
	    				break;
	    			}
	    		}
			}
			
			int totalPopulation = this.voters.size();
			int percentageCandidatePopulation = (int) (candidatePopulation*100)/totalPopulation;
			
	    	for(int j=0; j<this.states.size();j++){
	    		boolean foundChief = false;
	    		String[] chiefsStatesArray = this.candidates.get(i).getChiefsStates().toArray(new String[this.candidates.get(i).getChiefsStates().size()]);
	    		for(int k=0; k < chiefsStatesArray.length; k++){
	   			String ChiefsState = chiefsStatesArray[k];    			
	    			if(ChiefsState.equals(this.states.get(j))){
	    				foundChief = true;
	    				break;
	    			}
	    		}
	  
	    		boolean wonState=false;  		
	    		String stateWinner = this.statesObjects.get(this.states.get(j)).getWinner();
	    		if(stateWinner != null && stateWinner.equals(candidateId))
	    			wonState=true;
	    		
	    		int population = this.statesObjects.get(this.states.get(j)).getPopulation();
	   
	    		
	    		String changedBelief = null;
	    		String candidateChangedBelief = this.candidates.get(i).getChangedBelief();
	    		String stateBeliefToChange =  this.candidates.get(i).getStateBeliefToChange().get(this.states.get(j));
	    		
	    		if(!foundChief){
	    			changedBelief = "unknown";
	    		}else{
	    			if(candidateChangedBelief==null && stateBeliefToChange==null)
		    			changedBelief = "true";
		    		else if(candidateChangedBelief != null && stateBeliefToChange != null && candidateChangedBelief.equals(stateBeliefToChange))
		    			changedBelief = "true";
		    		else
		    			changedBelief = "false";
	    		}
	    		
	    		String mainStateBelief =  this.statesObjects.get(this.states.get(j)).getMainBelief();
	    		boolean sameBelief = false;
	    		if(mainStateBelief.equals(mainCandidateBelief))
	    			sameBelief = true;
	    		printWriter.printf("%s, %s, %b, %d, %b, %d, %d, %b, %s, %b, %d, %d, %d\n", candidateId, this.states.get(j), foundChief, population, wonState, credibility, stubbornness,  won, changedBelief, sameBelief, nChiefs, candidatePopulation, percentageCandidatePopulation);
	    		
	    	}	    	
	    }
	    printWriter.close();
	}

}
