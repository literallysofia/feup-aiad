package agentbehaviours;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.core.Agent;

public class Asking extends Behaviour{
	
	private Agent agent;
	private int n=0;
	
	public Asking(Agent a){
		this.agent = a;
	}

	public void action(){
		n++;
		System.out.println("Weelellele");
		
	}

	@Override
	public boolean done() {
		return n==2;
	}
}
