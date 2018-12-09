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
import jade.core.AID;
import jade.core.Agent;

public class ChiefSendVoterQuestion extends Behaviour {
	private ChiefOfStaff chiefOfStaff;
	private boolean finished;

	public ChiefSendVoterQuestion(ChiefOfStaff a) {
		this.chiefOfStaff = a;
	}

	public void action() {
		
		//this.chiefOfStaff.logger.info("> INFO:    ID: " +  this.chiefOfStaff.getLocalName() + " STATE: " + this.chiefOfStaff.getStateName() + " BOSS: " + this.chiefOfStaff.getBoss().getLocalName());
		System.out.println("> INFO:    ID: " +  this.chiefOfStaff.getLocalName() + " STATE: " + this.chiefOfStaff.getStateName() + " BOSS: " + this.chiefOfStaff.getBoss().getLocalName());
		

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
			this.chiefOfStaff.setNrVotersState(result.length);
			for (int j = 0; j < result.length; j++) {
				AID dest = result[j].getName();
				msg.addReceiver(dest);
				this.chiefOfStaff.send(msg);
				//this.chiefOfStaff.logger.info("SENT:      " + msg.getContentObject() + " TO: " + dest.getLocalName());
			}
		} catch (FIPAException e) {
			e.printStackTrace();
		} /*catch (UnreadableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		this.finished = true;

	}

	public boolean done() {
		return this.finished;
	}
}
