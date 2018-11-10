package agents;

import agentbehaviours.ListeningChiefOfStaffQuestion;
import agentbehaviours.SayWhatToChange;
import agentbehaviours.SendQuestion;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ChiefOfStaff extends Agent {
	
	private Candidate boss;
	private String state;
	private String chosenCandidate = "candidate_0";
	private String chosenBelief = "Comunism";
	

	public ChiefOfStaff(Candidate candidate, String state) {
		this.boss = candidate;
		this.state = state;
	}

	public Candidate getBoss() {
		return boss;
	}
	
	public String getStateName(){
		return this.state;
	}
	
	public void setState(String state){
		this.state = state;
	}

	public void setBoss(Candidate boss) {
		this.boss = boss;
	}

	public void setup() {
		System.out.println(" > CHIEF: " + this.getLocalName() + " STATE: " + this.state + " BOSS: " + this.boss.getId());
		addBehaviour(new SendQuestion(this));
		addBehaviour(new SayWhatToChange(this,MessageTemplate.MatchPerformative(ACLMessage.CFP)));
	}

	public String getChosenCandidate() {
		return chosenCandidate;
	}

	public void setChosenCandidate(String chosenCandidate) {
		this.chosenCandidate = chosenCandidate;
	}

	public String getChosenBelief() {
		return chosenBelief;
	}

	public void setChosenBelief(String chosenBelief) {
		this.chosenBelief = chosenBelief;
	}

}
