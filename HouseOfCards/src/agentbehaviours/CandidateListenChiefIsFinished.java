package agentbehaviours;

import java.util.ArrayList;

import agents.Candidate;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class CandidateListenChiefIsFinished extends SimpleBehaviour {

	private Candidate candidate;
	private boolean done = false;
	private int numberOfMessages = 0;

	public CandidateListenChiefIsFinished(Candidate a) {
		this.candidate = a;
	}

	@Override
	public void action() {

		if(this.candidate.getChiefsOfStaff().size()==0){
			this.candidate.addBehaviour(new CandidateSendBeliefs(this.candidate, 2));
			this.done=true;
			return;
		}
		
		ACLMessage msg = this.candidate.blockingReceive();
		if (msg != null) {
			
			if (msg.getSender().getLocalName().substring(0, 5).equals("chief")) {
				this.numberOfMessages++;
				//System.out.println(this.candidate.getLocalName() + "  " +  this.numberOfMessages + " " + msg.getSender().getLocalName());
				// System.out.println(" - CANDIDATE: " +
				// this.candidate.getLocalName() + " FINISHED: " +
				// msg.getSender().getLocalName());
				//this.candidate.logger.info("RECEIVED MSG: " + msg.getContent());
				//this.candidate.logger.info("RECEIVED:  " + msg.getContent() + " FROM: " + msg.getSender().getLocalName());
				
			}
		} else {
			block();
		}

		if (this.numberOfMessages == this.candidate.getChiefsOfStaff().size()) {
			this.done = true;
		}

	}

	@Override
	public boolean done() {
		return this.done;
	}

}
