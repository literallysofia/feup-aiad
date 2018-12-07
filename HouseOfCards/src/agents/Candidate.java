package agents;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
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
	private int stubbornness;
	private ArrayList<String> states = new ArrayList<String>();
	private HashMap<String, Integer> beliefs = new HashMap<>();
	private ArrayList<String> chiefsOfStaff = new ArrayList<>();
	private HashSet<String> chiefsStates =  new HashSet<String>();
	private HashMap<String, Integer> beliefToChangePopulation = new HashMap<>();
	private HashMap<String, Integer> beliefToChangeValue = new HashMap<>();
	private boolean won = false;

	public Candidate(String id, ArrayList<String> states, ArrayList<String> beliefs) {
		this.id = id;
		this.states = states;

		for (int i = 0; i < beliefs.size(); i++) {
			Random rnd = new Random();
			int value = rnd.nextInt(100) + 1;
			this.beliefs.put(beliefs.get(i), value);
		}

		Random rnd = new Random();
		this.stubbornness = rnd.nextInt(100) + 1;
		this.credibility = rnd.nextInt(20) + 80;
		setupLogger();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
	
	public int getStubbornness() {
		return stubbornness;
	}

	public void setStubbornness(int stubbornness) {
		this.stubbornness = stubbornness;
	}
	
	public boolean isWon() {
		return won;
	}

	public void setWon(boolean won) {
		this.won = won;
	}
	
	public HashSet<String> getChiefsStates() {
		return chiefsStates;
	}

	public void setChiefsStates(HashSet<String> chiefsStates) {
		this.chiefsStates = chiefsStates;
	}

	public void setupLogger() {

		this.logger = Logger.getLogger(this.id);
		FileHandler fh = null;
		this.logger.setUseParentHandlers(false);

		try {
			File logDir = new File("logs/");
			if (!(logDir.exists()))
				logDir.mkdir();
			long time = System.currentTimeMillis();
			fh = new FileHandler("logs/" + time + "_" + this.id + ".log");
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

		SequentialBehaviour loop = new SequentialBehaviour();
		loop.addSubBehaviour(new CandidateSendBeliefs(this, 1));
		SequentialBehaviour trial = new SequentialBehaviour();
		trial.addSubBehaviour(new CandidateListenChiefIsFinished(this));
		trial.addSubBehaviour(new CandidateListenChiefStatus(this, new ACLMessage(ACLMessage.CFP)));
		loop.addSubBehaviour(trial);
		addBehaviour(loop);

	}

	public void takeDown() {
		LogManager.getLogManager().reset();
		System.out.println(this.getLocalName() + " was taken down.");
	}

	// atualiza as beliefs conforme a informa��o dada pelo chief of staff
	public void changeBeliefs() {

		Random rnd = new Random();
		int changeMacro = rnd.nextInt(100) + 1;
		if (changeMacro > this.stubbornness) {
			if (this.beliefToChangePopulation.size() != 0 && this.beliefToChangeValue.size() != 0) {
				Map.Entry<String, Integer> maxEntry = null; // belief com mais
															// popula��o
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
															// mudan�a da cren�a
				// System.out.println("DIFF CRED: " + diffCre);

				this.credibility = this.credibility - diffCre;
				// System.out.println("NEW CREDIBILITY: " + this.credibility);

				this.logger.info("> INFO:    CHANGED BELIEF: " + maxEntry.getKey() + " TO " + value);
				this.logger.info("> INFO:    CHANGED credibility TO " + this.credibility + " BC macro: " + changeMacro);
				// System.out.println(
				// "> INFO: ID: " + this.getLocalName() + " CHANGED BELIEF: " +
				// maxEntry.getKey() + " TO " + value);
				// System.out.println("> INFO: ID: " + this.getLocalName() + " CHANGED
				// credibility TO " + this.credibility);

				/*
				 * System.out.println("                           - CANDIDATE: " +
				 * this.getLocalName() + " CHANGED BELIEF: " + maxEntry.getKey() +
				 * " OLD VALUE: " + oldValue + " NEW VALUE : " + value +
				 * " CHANGED CREDIBILITY: " + this.credibility);
				 */

			}
		} else {
			this.logger.info("> INFO:    DID NOT CHANGE BELIEFS BC macro: " + changeMacro);
		}

		this.addBehaviour(new CandidateSendBeliefs(this, 2));
	}

}
