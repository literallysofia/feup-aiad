package elections;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;
import java.util.Random;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;

public class Elections {
	
	private int minPopulation, maxPopulation, nrStates, nrCandidates;
	private Runtime rt;
	private Profile p;
	private ContainerController cc;
	private ArrayList<String> States = new ArrayList<String>();
	private ArrayList<String> Beliefs = new ArrayList<String>();

	public Elections(int minPopulation, int maxPopulation, int nrStates, int nrCandidates) throws StaleProxyException {
		
		this.minPopulation = minPopulation;
		this.maxPopulation = maxPopulation;
		this.nrStates = nrStates;
		this.nrCandidates = nrCandidates;
		
		this.rt = Runtime.instance();
		this.p = new ProfileImpl(true);
		this.cc =rt.createMainContainer(p);
		
		try{
			createVotersPerState();
			createCandidates();
			createChiefsOfStaff();
		}catch(Exception e){
			e.printStackTrace();
		}
	
	}
	
	public int getMinPopulation(){
		return this.minPopulation;
	}
	
	public int getMaxPopulation(){
		return this.maxPopulation;
	}
	
	public int getNrStates(){
		return this.nrStates;
	}
	
	//min_state_population, max_state_population, nr_states, nr_candidates
	public static void main(String args[]) throws StaleProxyException {
		Elections elections = new Elections(Integer.parseInt(args[0]), Integer.parseInt(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3]));
	}
	
	public void createVotersPerState() throws StaleProxyException{
		
		for(int id_state=0; id_state < this.nrStates; id_state++){
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
		
		System.out.println(" > CANDIDATES: " + this.nrCandidates);
		
		for(int id_candidate=0; id_candidate<this.nrCandidates; id_candidate++){
			String id = "candidate_"+Integer.toString(id_candidate);
			AgentController ac = this.cc.createNewAgent(id,"agents.Candidate",null);
			ac.start();
		}
			
	}
	
	public void createChiefsOfStaff() throws StaleProxyException{
		
		System.out.println(" > CHIEFS OF STAFF: " + this.nrStates);
		
		for(int id_chief=0; id_chief<this.nrStates; id_chief++){
			String id = "chiefofstaff_"+Integer.toString(id_chief);
			AgentController ac = this.cc.createNewAgent(id,"agents.ChiefOfStaff",null);
			ac.start();
		}
			
	}

}
