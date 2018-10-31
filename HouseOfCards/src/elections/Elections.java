package elections;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.util.Random;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;

public class Elections {
	
	private int minPopulation, maxPopulation, states, candidates;
	private Runtime rt;
	private Profile p;
	private ContainerController cc;
	

	public Elections(int minPopulation, int maxPopulation, int states, int candidates) throws StaleProxyException {
		this.minPopulation = minPopulation;
		this.maxPopulation = maxPopulation;
		this.states = states;
		this.candidates = candidates;
		this.rt = Runtime.instance();
		this.p = new ProfileImpl(true);
		this.cc =rt.createMainContainer(p);
		
		try{
		createVotersPerState();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		createCandidates();
	}
	
	public int getMinPopulation(){
		return this.minPopulation;
	}
	
	public int getMaxPopulation(){
		return this.maxPopulation;
	}
	
	public int getStates(){
		return this.states;
	}
	
	//min_state_population, max_state_population, nr_states, nr_candidates
	public static void main(String args[]) throws StaleProxyException {
		Elections elections = new Elections(Integer.parseInt(args[0]), Integer.parseInt(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3]));
	}
	
	
	public void createVotersPerState() throws StaleProxyException{
		
		for(int id_state=0; id_state < states; id_state++){
			int id_voterstate = 0;
			
			Random rnd = new Random();
			int population = rnd.nextInt(this.maxPopulation+1 - this.minPopulation) + this.minPopulation;
			System.out.println(" > STATE " + id_state + " POPULATION:" + population);
			
			while(id_voterstate < population ){
				try{
					String id = "voter_"+Integer.toString(id_state)+"_"+Integer.toString(id_voterstate);
					AgentController ac = this.cc.createNewAgent(id,"agents.Voter",null);
					ac.start();
					id_voterstate++;
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public void createCandidates() throws StaleProxyException{
		
		System.out.println(" > CANDIDATES: " + candidates);
		
		for(int id_candidate=0; id_candidate<candidates; id_candidate++){
			String id = "candidate_"+Integer.toString(id_candidate);
			AgentController ac = this.cc.createNewAgent(id,"agents.Candidate",null);
			ac.start();
		}
			
	}

}
