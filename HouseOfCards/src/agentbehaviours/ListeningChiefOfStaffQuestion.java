package agentbehaviours;

import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.util.ArrayList;
import java.util.HashMap;

import agents.Voter;
import jade.core.Agent;

public class ListeningChiefOfStaffQuestion extends SimpleBehaviour {

	private Voter voter;
	private boolean finished = false;

	public ListeningChiefOfStaffQuestion(Voter voter) {
		this.voter = voter;
	}

	public void action() {

		ACLMessage msg = this.voter.blockingReceive();

		if (msg != null) {
			if (msg.getSender().getLocalName().substring(0, 9).equals("chiefofst")) {

				try {
					System.out.println("   - VOTER: " + this.voter.getLocalName() + " LISTENING CHIEF OF STAFF BOSS: "
							+ msg.getSender().getLocalName() + " " + msg.getContentObject());
					
					ArrayList<String> message = (ArrayList) msg.getContentObject();
					String candidate = message.get(1);
					String belief = this.voter.calculateWrongBelief(candidate);
					System.out.println("BELIEF = " + belief);

				} catch (UnreadableException e) {
					e.printStackTrace();
				}
			}
		} else {
			block();
		}

		return;
	}

	public boolean done() {
		return this.finished;
	}

}
