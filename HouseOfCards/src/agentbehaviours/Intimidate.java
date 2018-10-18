package agentbehaviours;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.core.Agent;

public class Intimidate extends Behaviour{
	
		private Agent agent;
		
	public Intimidate(Agent a){
		this.agent=a;
	}
	
	   private int n =0;

	    public void action(){
	    	++n;
	    	switch(n){
	    	case 1: 
	    		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
	    		AID dest = new AID("ClaireUnderwood", AID.ISLOCALNAME);
	    		msg.setContent("Frank: Stay with me");	
	    		msg.addReceiver(dest);
	    		this.agent.send(msg);
	    	break;
	    	}
	        
	        
	    }


	    public boolean done(){
	        return n==2;
	    }
}
