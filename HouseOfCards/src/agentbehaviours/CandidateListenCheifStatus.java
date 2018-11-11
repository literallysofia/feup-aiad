package agentbehaviours;

import java.util.ArrayList;
import java.util.Vector;

import agents.Candidate;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetInitiator;

public class CandidateListenCheifStatus extends ContractNetInitiator {

	private Candidate candidate;

	public CandidateListenCheifStatus(Candidate a, ACLMessage cfp) {
		super(a, cfp);
		this.candidate = a;
	}

	protected Vector prepareCfps(ACLMessage cfp) {
		Vector v = new Vector();
		cfp.setContent("What should i change?");
		for (int i = 0; i < candidate.getChiefsOfStaff().size(); i++) {
			cfp.addReceiver(new AID(candidate.getChiefsOfStaff().get(i), false));
			this.candidate.logger.info("SENT:      " + cfp.getContent() + " TO: " + candidate.getChiefsOfStaff().get(i));
		}
		v.add(cfp);

		return v;
	}

	protected void handleAllResponses(Vector responses, Vector acceptances) {
		// System.out.println("got " + responses.size() + " responses!");

		for (int i = 0; i < responses.size(); i++) {
			ACLMessage msg;
			msg = (ACLMessage) ((ACLMessage) responses.elementAt(i));

			ArrayList<String> parseResponse = new ArrayList<>();
			try {
				parseResponse = (ArrayList<String>) msg.getContentObject();
				this.candidate.logger.info("RECEIVED:  " + parseResponse + " FROM: " + msg.getSender().getLocalName());
				
				//this.candidate.logger.info("RECEIVED MSG: " + parseResponse);
			} catch (UnreadableException e) {
				e.printStackTrace();
			}

			System.out.println("                     - CANDIDATE: " + this.candidate.getLocalName() + " WHAT TO CHANGE: "
					+ parseResponse);

			if (parseResponse.get(0).equals("Losing in ")) {
				String state = parseResponse.get(1);
				String belief = parseResponse.get(3);
				int value = Integer.parseInt(parseResponse.get(5));
				int population = getStatePopulation(state);

				if (candidate.getBeliefToChangePopulation() != null
						&& candidate.getBeliefToChangePopulation().get(belief) != null) {
					int old_pop = candidate.getBeliefToChangePopulation().get(belief);
					this.candidate.getBeliefToChangePopulation().put(belief, old_pop + population);
					int old_value = candidate.getBeliefToChangeValue().get(belief);
					int new_value = (old_value + value) / 2;
					this.candidate.getBeliefToChangeValue().put(belief, new_value);

				} else {
					this.candidate.getBeliefToChangePopulation().put(belief, population);
					this.candidate.getBeliefToChangeValue().put(belief, value);
				}

			}

		}

		this.candidate.changeBeliefs();
	}

	public int getStatePopulation(String state) {
		DFAgentDescription[] result = null;

		try {
			DFAgentDescription dfd = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType(state);
			dfd.addServices(sd);
			result = DFService.search(this.candidate, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}

		return result.length;
	}

}
