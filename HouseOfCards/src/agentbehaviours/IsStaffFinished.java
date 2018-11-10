package agentbehaviours;

import java.util.ArrayList;

import agents.Candidate;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class IsStaffFinished extends SimpleBehaviour{
		
	private Candidate candidate;
	private boolean done = false;
	private int numberOfMessages = 0;

	
	public IsStaffFinished(Candidate a){
		this.candidate = a;
	}

	@Override
	public void action() {
		ACLMessage msg = this.candidate.blockingReceive();
		if (msg != null) {
			this.numberOfMessages++;
			if (msg.getSender().getLocalName().substring(0, 5).equals("chief")) {				
					System.out.println(msg.getContent());
			}
		} else {
			block();
		}
		
		if(this.numberOfMessages == this.candidate.getChiefsOfStaff().size()){
			this.done=true;
		}
		
	}

	@Override
	public boolean done() {
		return this.done;
	}

}
