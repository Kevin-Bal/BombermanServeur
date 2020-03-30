package Agent;

import Strategies.Strategy;

public class BombermanFactory implements AgentFactory {

	@Override
	public Agent createAgent(int x, int y, char type, AgentAction agentAction, ColorAgent col, Strategy strategy) {
    	Agent agent = new Bomberman(x, y,agentAction, col, strategy);
    	return agent;
	}

}
