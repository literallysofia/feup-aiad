package agentbehaviours;

import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.util.ArrayList;
import java.util.HashMap;

import agents.ChiefOfStaff;
import agents.Voter;
import jade.core.Agent;

public class ChiefOfStaffListeningVoter extends SimpleBehaviour {

	private ChiefOfStaff chiefOfStaff;
	private boolean finished = false;

	public ChiefOfStaffListeningVoter(ChiefOfStaff chiefOfStaff) {
		this.chiefOfStaff = chiefOfStaff;
	}

	public void action() {

		ACLMessage msg = this.chiefOfStaff.blockingReceive();

		if (msg != null) {
			if (msg.getSender().getLocalName().substring(0, 5).equals("voter")) {

				try {
					ArrayList<String> message = (ArrayList) msg.getContentObject();
					String candidate = message.get(1);
					String belief = message.get(3);

					System.out.println("               - CHIEF OF STAFF: " + this.chiefOfStaff.getLocalName()
							+ " LISTENING VOTER CHOICES: " + msg.getSender().getLocalName() + " " + message);

					this.chiefOfStaff.getStateChosenCandidates().add(candidate);
					this.chiefOfStaff.getStateChosenBeliefs().add(belief);

				} catch (UnreadableException e) {
					e.printStackTrace();
				}
			}
		} else {
			block();
		}

		if (this.chiefOfStaff.getStateChosenCandidates().size() == this.chiefOfStaff.getNrVotersState()) {
			this.chiefOfStaff.calculateChosens();
			this.finished = true;
		}

		return;
	}

	public boolean done() {
		return this.finished;
	}

}
