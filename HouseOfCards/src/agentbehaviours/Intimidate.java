package agentbehaviours;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.AMSService;

public class Intimidate extends Behaviour{
	
		private Agent agent;
		
	public Intimidate(Agent a){
		this.agent=a;
	}
	
	public AMSAgentDescription[] findAgents(){
		
		AMSAgentDescription [] agents = null;
		
		try{
			SearchConstraints c = new SearchConstraints();
			c.setMaxResults(new Long(-1));
			agents = AMSService.search(this.agent,  new AMSAgentDescription(),c);
			
		}catch(Exception e){
			System.out.println("Exception caught");
		}
		
		return agents;
		
	}
	
	   private int n =0;

	    public void action(){
	    	++n;
	    	switch(n){
	    	case 1: 
	    		AMSAgentDescription[] agents = findAgents();
	    		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
	    		for(int i = 0; i < agents.length;i ++){
	    			
	    			AID agentId = agents[i].getName();
	    			System.out.println(agentId);
	    			msg.setContent("Frank: Stay with me");	
		    		msg.addReceiver(agentId);
		    		this.agent.send(msg);
	    		}
	    	//	AID dest = new AID("ClaireUnderwood", AID.ISLOCALNAME);
	    		
	    	break;
	    	}
	        
	        
	    }


	    public boolean done(){
	        return n==2;
	    }
}
