package agentbehaviours;

import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import agents.Candidate;
import jade.core.Agent;

public class SendBeliefs extends Behaviour{
	private Candidate candidate;
	private int n = 0;

	public SendBeliefs(Candidate a) {
		this.candidate = a;
	}

	public void action() {
		for(int i = 0; i< this.candidate.getStates().size();i++){
		    DFAgentDescription dfd = new DFAgentDescription();
            ServiceDescription sd  = new ServiceDescription();
            sd.setType( this.candidate.getStates().get(i) );
            dfd.addServices(sd);
            
            try{
            	DFAgentDescription[] result = DFService.search(this.candidate, dfd);
            	for(int j =0; j < result.length; j++){
            	}
            }catch(FIPAException e){
            	e.printStackTrace();
            }
           
		}
		n = 1;
	}

	@Override
	public boolean done() {
		return n == 1;
	}
}
