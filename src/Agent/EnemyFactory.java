package Agent;

import Strategies.Strategy;

public class EnemyFactory implements AgentFactory {
	private Agent agent;
	
	@Override
	public Agent createAgent(int x, int y, char type, AgentAction agentAction, ColorAgent col, Strategy strategy) {
		switch(type) {
			case 'E' : 
				agent = new Enemy(x, y, agentAction, strategy);
				break;
			case 'V' :
				agent = new Bird(x, y, agentAction, strategy);
				break;
			case 'R' :
				agent = new Rajion(x, y, agentAction, strategy);
				break;
			default :
				break;
		}
		
		return agent;
	}

}
