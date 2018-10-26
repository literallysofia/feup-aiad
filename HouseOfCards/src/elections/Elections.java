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
	

	public Elections(int minPopulation, int maxPopulation, int states, int candidates) {
		this.minPopulation = minPopulation;
		this.maxPopulation = maxPopulation;
		this.states = states;
		this.candidates = candidates;
		this.rt = Runtime.instance();
		this.p = new ProfileImpl(true);
		this.cc =rt.createMainContainer(p);
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
	
	public static void main(String args[]) throws StaleProxyException{
		Elections elections = new Elections(Integer.parseInt(args[0]), Integer.parseInt(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3]));
		Random rnd = new Random();
		int population = rnd.nextInt(elections.getMaxPopulation()+1 - elections.getMinPopulation()) + elections.getMinPopulation();
		System.out.println(population);
		
		try{
			
		elections.createAgentsPerState(elections.getStates(), population);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public void createAgents(int population)throws StaleProxyException{
		//Object[] args = new Object[1];
		int i = 0;
		while(i < population ){
			//args[0]=i+1;
			try{
				System.out.println("reaching here with population: " + population);
			AgentController ac = this.cc.createNewAgent("voter"+Integer.toString(i),"agents.Voter",null);
			ac.start();
			i++;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
	
	public void createAgentsPerState(int states, int population)throws StaleProxyException{
		for(int i=0; i < states; i++){
			try{
			createAgents(population);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

}
