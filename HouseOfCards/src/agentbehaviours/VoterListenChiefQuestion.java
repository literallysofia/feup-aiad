package agentbehaviours;

import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.util.ArrayList;
import java.util.HashMap;

import agents.Voter;
import jade.core.Agent;

public class VoterListenChiefQuestion extends SimpleBehaviour {

	private Voter voter;
	private boolean finished = false;

	public VoterListenChiefQuestion(Voter voter) {
		this.voter = voter;
	}

	public void action() {

		ACLMessage msg = this.voter.blockingReceive();

		if (msg != null) {
			if (msg.getSender().getLocalName().substring(0, 9).equals("chiefofst")) {

				try {
					System.out.println("   - VOTER: " + this.voter.getLocalName() + " LISTENING CHIEF OF STAFF BOSS: "
							+ msg.getSender().getLocalName() + " " + msg.getContentObject());
					this.voter.logger.info("RECEIVED:  " + msg.getContentObject() + " FROM: " + msg.getSender().getLocalName());
					ArrayList<String> message = (ArrayList) msg.getContentObject();
					String candidate = message.get(1);
					this.voter.getChiefOfStaffInfo().put(msg.getSender().getLocalName(), candidate);

				} catch (UnreadableException e) {
					e.printStackTrace();
				}
			}
		} else {
			block();
		}
		this.finished = true;
		return;
	}

	public boolean done() {
		return this.finished;
	}

}
