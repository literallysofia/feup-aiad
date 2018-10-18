package agentbehaviours;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.core.Agent;




public class Asking extends CyclicBehaviour{
	
	private Agent agent;
	
	public Asking(Agent a){
		this.agent = a;
	}

	public void action(){
		ACLMessage msg = this.agent.receive();
		if(msg != null){
			System.out.println(msg.getContent());
			System.out.println(this.agent.getLocalName() + "I can't go back to being the First-Lady, " + msg.getSender().getLocalName() );
			ACLMessage reply = msg.createReply();
			reply.setPerformative(ACLMessage.INFORM);
			reply.setContent("I see.");
			this.agent.send(reply);
		}
		else {
			block();
		}
	}
}
