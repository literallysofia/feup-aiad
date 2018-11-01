package agentbehaviours;

import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.core.Agent;

public class ListeningCandidateBeliefs extends CyclicBehaviour{
	
	
	private Agent agent;
	private int n = 0;

	public ListeningCandidateBeliefs(Agent a) {
		this.agent = a;
	}

	public void action() {
		ACLMessage msg = this.agent.blockingReceive();
		try {
			System.out.println("> Listening Candidate Beliefs: " + msg.getContentObject());
		} catch (UnreadableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



}
