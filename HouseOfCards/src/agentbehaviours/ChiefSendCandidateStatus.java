package agentbehaviours;

import agents.ChiefOfStaff;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;

import java.io.IOException;
import java.util.ArrayList;

public class ChiefSendCandidateStatus extends ContractNetResponder {

	private ChiefOfStaff chiefOfStaff;

	public ChiefSendCandidateStatus(ChiefOfStaff a, MessageTemplate mt) {
		super(a, mt);
		this.chiefOfStaff = a;
	}

	protected ACLMessage handleCfp(ACLMessage cfp) {
		//System.out.println("                  - CHIEF OF STAFF: " + this.chiefOfStaff.getLocalName()
				//+ " SAY WHAT TO CHANGE: " + cfp.getSender().getLocalName() + " " + cfp.getContent());
		ACLMessage reply = cfp.createReply();
		reply.setPerformative(ACLMessage.PROPOSE);
		if (this.chiefOfStaff.getChosenCandidate() != null) {

			if (this.chiefOfStaff.getChosenCandidate().equals(this.chiefOfStaff.getBoss().getId())) {
				ArrayList<String> answer = new ArrayList();
				answer.add("Winning in ");
				answer.add(this.chiefOfStaff.getStateName());
				try {
					reply.setContentObject(answer);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				ArrayList<String> changing = new ArrayList<>();
				changing.add("Losing in ");
				changing.add(this.chiefOfStaff.getStateName());
				changing.add(" and should change your ");
				changing.add(this.chiefOfStaff.getChosenBelief());
				changing.add(" to ");
				changing.add(this.chiefOfStaff.getChosenValue()+"");
				try {
					reply.setContentObject(changing);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			ArrayList<String> nullCandidate = new ArrayList<>();
			nullCandidate.add("Losing in ");
			nullCandidate.add(this.chiefOfStaff.getStateName());
			nullCandidate.add(" and should change your ");
			nullCandidate.add(this.chiefOfStaff.getChosenBelief());
			nullCandidate.add(" to ");
			nullCandidate.add(this.chiefOfStaff.getChosenValue()+"");

			try {
				reply.setContentObject(nullCandidate);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return reply;
	}

}
