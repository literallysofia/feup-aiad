package agentbehaviours;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.core.Agent;

public class Asking extends Behaviour{
	
	private Agent agent;
	
	public Asking(Agent a){
		this.agent = a;
	}

	public void action(){
		System.out.println("Weelellele");
		
	}

	@Override
	public boolean done() {
		return false;
	}
}
