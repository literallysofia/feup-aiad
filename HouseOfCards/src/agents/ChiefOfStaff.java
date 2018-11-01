package agents;

import jade.core.Agent;
import jade.core.behaviours.*;
import agentbehaviours.Asking;

public class ChiefOfStaff extends Agent{
	
	private Candidate boss;

	public ChiefOfStaff() {
		// TODO Auto-generated constructor stub
	}

	public Candidate getBoss() {
		return boss;
	}

	public void setBoss(Candidate boss) {
		this.boss = boss;
	}
	
	public void setup(){
        addBehaviour(new Asking(this));
    }

}
