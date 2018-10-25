package elections;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
public class Elections {

	public Elections() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String args[]) throws StaleProxyException{
		Runtime rt = Runtime.instance();
		Profile p = new ProfileImpl(true);
		ContainerController cc =rt.createMainContainer(p);
		AgentController ac = cc.createNewAgent("voter","agents.Voter",null);
		ac.start();
	}

}
