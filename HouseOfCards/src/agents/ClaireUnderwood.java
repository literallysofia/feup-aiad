package agents;
import jade.core.behaviours.*;
import agentbehaviours.Asking;
import agentbehaviours.Intimidate;
import jade.core.Agent;

public class ClaireUnderwood extends Agent{
	
	public void setup(){
        addBehaviour(new Asking(this));
       }

    public void takeDown(){
        System.out.println(getLocalName()+ ": You won, Frank.");
    }

}
