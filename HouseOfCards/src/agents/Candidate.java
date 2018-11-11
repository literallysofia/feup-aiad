package agents;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import agentbehaviours.CandidateListenChiefIsFinished;
import agentbehaviours.CandidateSendBeliefs;
import agentbehaviours.CandidateListenChiefStatus;

public class Candidate extends Agent { 
	public Logger logger;
	private String id;
	private int credibility = 100;
	private ArrayList<String> states = new ArrayList<String>();
	private HashMap<String, Integer> beliefs = new HashMap<>();
	private ArrayList<String> chiefsOfStaff = new ArrayList<>();
	private HashMap<String, Integer> beliefToChangePopulation = new HashMap<>();
	private HashMap<String, Integer> beliefToChangeValue = new HashMap<>();

	public Candidate(String id, ArrayList<String> states, ArrayList<String> beliefs) {
		this.id=id;
		this.states = states;

		for (int i = 0; i < beliefs.size(); i++) {
			Random rnd = new Random();
			int value = rnd.nextInt(100) + 1;
			this.beliefs.put(beliefs.get(i), value);
		}
		setupLogger();
	}


	public ArrayList<String> getStates() {
		return states;
	}

	public void setStates(ArrayList<String> states) {
		this.states = states;
	}

	public HashMap<String, Integer> getBeliefs() {
		return beliefs;
	}

	public void setBeliefs(HashMap<String, Integer> beliefs) {
		this.beliefs = beliefs;
	}

	public ArrayList<String> getChiefsOfStaff() {
		return chiefsOfStaff;
	}

	public void setChiefsOfStaff(ArrayList<String> chiefsOfStaff) {
		this.chiefsOfStaff = chiefsOfStaff;
	}

	public int getCredibility() {
		return credibility;
	}

	public void setCredibility(int credibility) {
		this.credibility = credibility;
	}

	public HashMap<String, Integer> getBeliefToChangePopulation() {
		return beliefToChangePopulation;
	}

	public void setBeliefToChangePopulation(HashMap<String, Integer> beliefToChangePopulation) {
		this.beliefToChangePopulation = beliefToChangePopulation;
	}

	public HashMap<String, Integer> getBeliefToChangeValue() {
		return beliefToChangeValue;
	}

	public void setBeliefToChangeValue(HashMap<String, Integer> beliefToChangeValue) {
		this.beliefToChangeValue = beliefToChangeValue;
	}
	
	public void setupLogger(){
		
		this.logger=Logger.getLogger(this.id);
	    FileHandler fh = null; 
	    this.logger.setUseParentHandlers(false);

	    try {  
	    	File logDir = new File("logs/"); 
			if( !(logDir.exists()) )
				logDir.mkdir();
		
	        fh = new FileHandler("logs/"+this.id+".log");  
	        this.logger.addHandler(fh); 
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter); 

	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	   
	}

	public void setup() {
		this.logger.info("> INFO:    ID: " +  this.getLocalName() + " CREDIBILITY: " + this.credibility + " BELIEFS: " + this.beliefs);
		System.out.println("> INFO:    ID: " +  this.getLocalName() + " CREDIBILITY: " + this.credibility + " BELIEFS: " + this.beliefs);
		addBehaviour(new CandidateSendBeliefs(this));
		SequentialBehaviour trial = new SequentialBehaviour();
		trial.addSubBehaviour(new CandidateListenChiefIsFinished(this));
		trial.addSubBehaviour(new CandidateListenChiefStatus(this, new ACLMessage(ACLMessage.CFP)));
		addBehaviour(trial);
		//this.logger.getHandlers()[0].close();
	}

	// atualiza as beliefs conforme a informação dada pelo chief of staff
	public void changeBeliefs() {

		if (this.beliefToChangePopulation.size() != 0 && this.beliefToChangeValue.size() != 0) {
			Map.Entry<String, Integer> maxEntry = null; // belief com mais
														// população
			for (Map.Entry<String, Integer> entry : this.beliefToChangePopulation.entrySet()) {
				if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
					maxEntry = entry;
				}
			}
			// System.out.println("BELIEFS_POP: " +
			// this.beliefToChangePopulation);
			// System.out.println("BELIEFS_VALUE: " + this.beliefToChangeValue);

			// System.out.println("OLD BELIEFS: " + this.beliefs);
			int value = this.beliefToChangeValue.get(maxEntry.getKey());
			int oldValue = this.beliefs.get(maxEntry.getKey());
			this.beliefs.replace(maxEntry.getKey(), value);

			// System.out.println("NEW BELIEFS: " + this.beliefs);
			// System.out.println("OLD CREDIBILITY: " + this.credibility);

			int diff = Math.abs(oldValue - value);
			// System.out.println("DIFF BELIEF: " + diff);

			int diffCre = (int) Math.ceil(diff / 4.0); // credibilidade perde um
														// quarto do valor da
														// mudança da crença
			// System.out.println("DIFF CRED: " + diffCre);

			this.credibility = this.credibility - diffCre;
			// System.out.println("NEW CREDIBILITY: " + this.credibility);

			this.logger.info("> INFO:    CHANGED BELIEF: " + maxEntry.getKey() + " TO " + value);  
			this.logger.info("> INFO:    CHANGED credibility TO " + this.credibility);  
			System.out.println("> INFO:    ID: " + this.getLocalName()+ " CHANGED BELIEF: " + maxEntry.getKey() + " TO " + value);  
			System.out.println("> INFO:    ID: " + this.getLocalName()+ " CHANGED credibility TO " + this.credibility);  
			
			/*System.out.println("                           - CANDIDATE: " + this.getLocalName() + " CHANGED BELIEF: "
					+ maxEntry.getKey() + " OLD VALUE: " + oldValue + " NEW VALUE : " + value + " CHANGED CREDIBILITY: "
					+ this.credibility);*/

		}
	}

}

