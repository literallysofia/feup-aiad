package agentbehaviours;

import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.ArrayList;

import agents.Candidate;
import agents.ChiefOfStaff;
import agents.Voter;
import jade.core.AID;
import jade.core.Agent;

public class AnswerChiefOfStaff extends SimpleBehaviour {

	private Voter voter;
	private boolean finished = false;

	public AnswerChiefOfStaff(Voter voter) {
		this.voter = voter;
	}

	@Override
	public void action() {

		String belief = this.voter.calculateWrongBelief();

		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		try {
			ArrayList<String> info = new ArrayList();
			info.add("I'll vote in candidate: ");
			info.add(this.voter.getChosenCandidate());
			info.add("I'll consider changing my vote if your boss changes the belief: ");
			info.add(belief);

			msg.setContentObject(info);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		AID dest = new AID ((String) this.voter.getChiefOfStaffInfo().keySet().toArray()[0], false);
		msg.addReceiver(dest);
		this.voter.send(msg);
		
		try {
			System.out.println("            - VOTER: " + this.voter.getLocalName() + " " + msg.getContentObject());
		} catch (UnreadableException e) {
			e.printStackTrace();
		}

		this.finished = true;

	}

	@Override
	public boolean done() {
		return this.finished;
	}

}
