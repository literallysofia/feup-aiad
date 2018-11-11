package agentbehaviours;

import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.util.ArrayList;
import java.util.HashMap;

import agents.Voter;
import jade.core.Agent;

public class VoterListeningCandidate extends SimpleBehaviour {

	private Voter voter;
	private boolean finished = false;

	public VoterListeningCandidate(Voter voter) {
		this.voter = voter;
	}

	public void action() {

		ACLMessage msg = this.voter.blockingReceive();

		if (msg != null) {
			try {
				if (msg.getSender().getLocalName().substring(0,9).equals("candidate")) {
					//System.out.println("   - VOTER: " + this.voter.getLocalName() + " LISTENING CANDIDATE BELIEFS: "
						//	+ msg.getSender().getLocalName() + " " + msg.getContentObject());
					String candidate = msg.getSender().getLocalName();
					HashMap<String, Integer> beliefs = new HashMap<String, Integer>();
					HashMap<String, Integer> credibility = new HashMap<String, Integer>();
					ArrayList<HashMap<String, Integer>> profile = new ArrayList<HashMap<String, Integer>>();
					profile = (ArrayList) msg.getContentObject();
					this.voter.getCandidatesBeliefs().put(candidate, profile.get(0));
					this.voter.getCandidatesCredibility().put(msg.getSender().getLocalName(),
							profile.get(1).get("Credibility"));
				}
			} catch (UnreadableException e) {
				e.printStackTrace();
			}
		} else {
			block();
		}

		if (this.voter.getCandidatesBeliefs().size() == this.voter.getCandidatesSize()) {
			this.voter.chooseCandidate();
			this.finished = true;
		}

		return;
	}

	public boolean done() {
		return this.finished;
	}

}
