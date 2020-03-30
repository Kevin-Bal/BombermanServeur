package Agent;

import Strategies.Strategy;

public interface AgentFactory {
	public Agent createAgent(int x, int y, char type, AgentAction agentAction, ColorAgent col, Strategy strategy);
}
