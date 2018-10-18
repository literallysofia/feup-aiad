package agents;
import jade.core.behaviours.*;
import agentbehaviours.Intimidate;
import jade.core.Agent;


public class FrankUnderwood extends Agent{
	
	public void setup(){
        addBehaviour(new Intimidate(this));
       }

    public void takeDown(){
        System.out.println(getLocalName()+ ": I won.");
    }

}
