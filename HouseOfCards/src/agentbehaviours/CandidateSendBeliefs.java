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
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import agents.Candidate;
import jade.core.AID;
import jade.core.Agent;

public class CandidateSendBeliefs extends Behaviour {
	private Candidate candidate;
	private boolean finished;
	private int cycle;

	public CandidateSendBeliefs(Candidate a, int cycle) {
		this.candidate = a;
		this.cycle = cycle;
		
		this.candidate.logger.info("> INFO:    ID: " + this.candidate.getLocalName() + " CREDIBILITY: " + this.candidate.getCredibility() + " BELIEFS: "
				+ this.candidate.getBeliefs());
		System.out.println("> INFO:    ID: " + this.candidate.getLocalName() + " CREDIBILITY: " + this.candidate.getCredibility() + " BELIEFS: "
				+ this.candidate.getBeliefs());
		
		this.candidate.setBeliefToChangePopulation(new HashMap<>());
		this.candidate.setBeliefToChangeValue(new HashMap<>());
	}

	public void action() {
		
		//System.out.println("ID: " +  this.candidate.getLocalName() + " CREDIBILITY: " + this.candidate.getCredibility() + " BELIEFS:" + this.candidate.getBeliefs());  
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		try {
			ArrayList<HashMap<String, Integer>> profile = new ArrayList();

			HashMap<String, Integer> credibilityHash = new HashMap<>();
			credibilityHash.put("Credibility", this.candidate.getCredibility());
			profile.add(this.candidate.getBeliefs());
			profile.add(credibilityHash);

			msg.setContentObject(profile);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		for (int i = 0; i < this.candidate.getStates().size(); i++) {
			DFAgentDescription dfd = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType(this.candidate.getStates().get(i));
			dfd.addServices(sd);

			try {
				DFAgentDescription[] result = DFService.search(this.candidate, dfd);
				for (int j = 0; j < result.length; j++) {
					AID dest = result[j].getName();
					msg.addReceiver(dest);
					this.candidate.send(msg);
					this.candidate.logger.info("SENT:      " + msg.getContentObject() + " TO: " + dest.getLocalName());
				}
			} catch (FIPAException e) {
				e.printStackTrace();
			} catch (UnreadableException e) {
				e.printStackTrace();
			}

		}
		this.finished = true;
	}

	public boolean done() {
		return this.finished;
	}
}
