package agents;

import jade.core.Agent;
import jade.core.behaviours.*;

public class ChiefOfStaff extends Agent {
	
	private Candidate boss;
	private String state;

	public ChiefOfStaff(Candidate candidate, String state) {
		this.boss = candidate;
		this.state = state;
	}

	public Candidate getBoss() {
		return boss;
	}

	public void setBoss(Candidate boss) {
		this.boss = boss;
	}

	public void setup() {
		System.out.println(" > CHIEF: " + this.getLocalName() + " STATE: " + this.state + " BOSS: " + this.boss.getId());
	}

}
