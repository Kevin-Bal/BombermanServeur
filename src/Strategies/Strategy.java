package Strategies;

import Agent.Agent;
import Agent.AgentAction;
import Controler.GameState;

public interface Strategy {
	public AgentAction chooseAction(Agent agent, GameState game);
}
