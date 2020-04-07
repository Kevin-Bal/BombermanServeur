package Strategies;

import Agent.Agent;
import Agent.AgentAction;
import Controler.GameState;

import java.io.Serializable;

public interface Strategy {
	public AgentAction chooseAction(Agent agent, GameState game);
}
