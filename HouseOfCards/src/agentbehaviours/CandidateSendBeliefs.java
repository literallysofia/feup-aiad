package agentbehaviours;

import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import agents.Candidate;
import jade.core.AID;
import jade.core.Agent;

public class CandidateSendBeliefs extends Behaviour{
	private Candidate candidate;
	private boolean finished;

	public CandidateSendBeliefs(Candidate a) {
		this.candidate = a;
	}

	public void action() {
		
		ACLMessage msg = new ACLMessage( ACLMessage.INFORM );
	    try {
	    	ArrayList<HashMap<String,Integer>> profile = new ArrayList();
	    	
	    	HashMap<String, Integer> credibilityHash = new HashMap<>();
			credibilityHash.put("Credibility", this.candidate.getCredibility());
			profile.add(this.candidate.getBeliefs());
			profile.add(credibilityHash);
			
			msg.setContentObject(profile);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		for(int i = 0; i< this.candidate.getStates().size();i++){
		    DFAgentDescription dfd = new DFAgentDescription();
            ServiceDescription sd  = new ServiceDescription();
            sd.setType( this.candidate.getStates().get(i) );
            dfd.addServices(sd);
            
            try{
            	DFAgentDescription[] result = DFService.search(this.candidate, dfd);
            	for(int j =0; j < result.length; j++){
            		AID dest = result[j].getName();
            		msg.addReceiver(dest);
            		this.candidate.send(msg);
            	}
            }catch(FIPAException e){
            	e.printStackTrace();
            }
           
		}
		this.finished = true;
	}

	public  boolean done() {  return this.finished;  }
}
