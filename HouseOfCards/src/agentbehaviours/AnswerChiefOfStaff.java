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
import agents.Voter;
import jade.core.AID;
import jade.core.Agent;

public class AnswerChiefOfStaff extends SimpleBehaviour {
	
	private Voter voter;
	private boolean finished = false;
	
	public AnswerChiefOfStaff(Voter voter){
		this.voter = voter;
	}

	@Override
	public void action() {
		
		String belief = this.voter.calculateWrongBelief();
		
		System.out.println("         - VOTER:" + this.voter.getLocalName() + " CHOSEN BELIEF FOR CANDIDATE " + this.voter.getChiefOfStaffInfo().get(this.voter.getChiefOfStaffInfo().keySet().toArray()[0])
		+ ": " + belief);
		
		this.finished = true;
		
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return this.finished;
	}
	
	

}
