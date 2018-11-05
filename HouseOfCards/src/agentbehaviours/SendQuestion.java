package agentbehaviours;

import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.ArrayList;

import agents.Candidate;
import agents.ChiefOfStaff;
import jade.core.AID;
import jade.core.Agent;

public class SendQuestion extends Behaviour {
	private ChiefOfStaff chiefOfStaff;
	private boolean finished;

	public SendQuestion(ChiefOfStaff a) {
		this.chiefOfStaff = a;
	}

	public void action() {

		ACLMessage msg = new ACLMessage(ACLMessage.QUERY_IF);
		try {
			ArrayList<String> query = new ArrayList();
			query.add("My boss is ");
			query.add(this.chiefOfStaff.getBoss().getLocalName());
			query.add("Who's getting your vote?");

			msg.setContentObject(query);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(this.chiefOfStaff.getStateName());
		dfd.addServices(sd);

		try {
			DFAgentDescription[] result = DFService.search(this.chiefOfStaff, dfd);
			for (int j = 0; j < result.length; j++) {
				AID dest = result[j].getName();
				msg.addReceiver(dest);
				this.chiefOfStaff.send(msg);
			}
		} catch (FIPAException e) {
			e.printStackTrace();
		}

		this.finished = true;

	}

	public boolean done() {
		return this.finished;
	}
}
