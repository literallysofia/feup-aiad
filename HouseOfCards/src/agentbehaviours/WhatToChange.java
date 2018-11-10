package agentbehaviours;

import java.util.ArrayList;
import java.util.Vector;

import agents.Candidate;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetInitiator;

public class WhatToChange extends ContractNetInitiator {
	
	private Candidate candidate;

	public WhatToChange(Candidate a, ACLMessage cfp) {
		super(a, cfp);
		this.candidate = a;
	}
	
	protected Vector prepareCfps(ACLMessage cfp) {
		Vector v = new Vector();
		System.out.println("welelele " + this.candidate.getId() + " candidates" + this.candidate.getChiefsOfStaff().size());
		for(int i=0; i < candidate.getChiefsOfStaff().size(); i++){
			System.out.println(candidate.getChiefsOfStaff().get(i));
			cfp.addReceiver(new AID(candidate.getChiefsOfStaff().get(i),false));
		}
		
		cfp.setContent("What should i change?");
		v.add(cfp);
		

		return v;
	}
	
	protected void handleAllResponses(Vector responses, Vector acceptances) {
		
		System.out.println("got " + responses.size() + " responses!");
		
		for(int i=0; i<responses.size(); i++) {
			ACLMessage msg;
			msg = (ACLMessage) ((ACLMessage) responses.elementAt(i));

			ArrayList<String> parseResponses = new ArrayList<>();
			try {
				parseResponses = (ArrayList<String>) msg.getContentObject();
			} catch (UnreadableException e) {
				e.printStackTrace();
			}
			if(parseResponses.size() < 3){
				System.out.println("Candidate " + this.candidate.getId() + " is winning in " + parseResponses.get(1));
			}else{
				System.out.println("CANDIDATE " + this.candidate.getId() + " has to change " + parseResponses.get(3) + " because he's losin in " + parseResponses.get(1));
			}
			//ACLMessage msg = ((ACLMessage) responses.get(i)).createReply();
			//msg.setPerformative(ACLMessage.ACCEPT_PROPOSAL); // OR NOT!
			//acceptances.add(msg);
		}
	}
	
	

}
