package agentbehaviours;

import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.util.ArrayList;
import java.util.HashMap;

import agents.Voter;
import jade.core.Agent;

public class ListeningCandidateBeliefs extends CyclicBehaviour {

	private Voter voter;
	private int n = 0;

	public ListeningCandidateBeliefs(Voter voter) {
		this.voter = voter;
	}

	public void action() {

		while (this.voter.getCandidatesBeliefs().size() < this.voter.getCandidatesSize()) {

			try {
				ACLMessage msg = this.voter.blockingReceive();

				System.out.println("   - Listening Candidate Beliefs: " + msg.getSender().getLocalName() + " "
						+ msg.getContentObject());

				String candidate = msg.getSender().getLocalName();
				HashMap<String, Integer> beliefs = new HashMap<String, Integer>();
				beliefs = (HashMap) msg.getContentObject();
				this.voter.getCandidatesBeliefs().put(candidate, beliefs);

			} catch (UnreadableException e) {
				e.printStackTrace();
			}
		}
		
		block();

		System.out.println("      - " + this.voter.getCandidatesBeliefs());

		return;

	}

}
