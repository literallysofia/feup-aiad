package agentbehaviours;

import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.util.ArrayList;
import java.util.HashMap;

import agents.Voter;
import jade.core.Agent;

public class VoterListenCandidateAndChief extends SimpleBehaviour {

	private Voter voter;
	private boolean finished = false;
	private boolean chiefReceived = false;
	private int cycle;

	public VoterListenCandidateAndChief(Voter voter, int cycle) {
		this.voter = voter;
		this.cycle = cycle;

		this.voter.logger.info("> INFO:    ID: " + this.voter.getLocalName() + " BELIEFS: " + this.voter.getBeliefs() + " MIN CREDIBILITY: "
				+ this.voter.getMinCredibility());
		//System.out.println("> INFO:    ID: " + this.voter.getLocalName() + " BELIEFS: " + this.voter.getBeliefs() + " MIN CREDIBILITY: "
			//	+ this.voter.getMinCredibility());

		this.voter.setCandidatesbeliefs(new HashMap<>());
		this.voter.setCandidatesCredibility(new HashMap<>());
		this.voter.setChosenCandidate(null);
	}

	public void action() {

		ACLMessage msg = this.voter.blockingReceive();

		if (msg != null) {
			try {
				if (msg.getSender().getLocalName().substring(0, 9).equals("candidate")) {

					this.voter.logger
							.info("RECEIVED:  " + msg.getContentObject() + " FROM: " + msg.getSender().getLocalName());
					String candidate = msg.getSender().getLocalName();
					HashMap<String, Integer> beliefs = new HashMap<String, Integer>();
					HashMap<String, Integer> credibility = new HashMap<String, Integer>();
					ArrayList<HashMap<String, Integer>> profile = new ArrayList<HashMap<String, Integer>>();
					profile = (ArrayList) msg.getContentObject();
					this.voter.getCandidatesBeliefs().put(candidate, profile.get(0));
					this.voter.getCandidatesCredibility().put(msg.getSender().getLocalName(),
							profile.get(1).get("Credibility"));

				} else if (msg != null && msg.getSender().getLocalName().substring(0, 9).equals("chiefofst")) {

					try {
						this.voter.logger.info(
								"RECEIVED:  " + msg.getContentObject() + " FROM: " + msg.getSender().getLocalName());
						ArrayList<String> message = (ArrayList) msg.getContentObject();
						String candidate = message.get(1);
						this.voter.getChiefOfStaffInfo().put(msg.getSender().getLocalName(), candidate);
						chiefReceived = true;
					} catch (UnreadableException e) {
						e.printStackTrace();
					}
				}

			} catch (UnreadableException e) {
				e.printStackTrace();
			}
		} else {
			block();
		}
		if (this.cycle==1 && this.voter.getCandidatesBeliefs().size() == this.voter.getCandidatesSize() && chiefReceived) {
			this.voter.chooseCandidate();
			this.finished = true;
		}
		if(this.cycle==2 && this.voter.getCandidatesBeliefs().size() == this.voter.getCandidatesSize()){
			this.voter.chooseCandidate();
			this.voter.setReadyToVote(true);
			this.finished = true;
			//this.voter.doDelete();
		}

		return;
	}

	public boolean done() {
		return this.finished;
	}

}
