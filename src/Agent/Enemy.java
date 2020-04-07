package Agent;


import Controler.GameState;
import Strategies.Strategy;
import Controler.Map;


public class Enemy extends Agent {

	public Enemy(int x, int y, AgentAction agentAction, Strategy strategy) {
		super(x, y, agentAction, 'E', ColorAgent.DEFAULT, false, false, strategy);
	}

	public void executeAction(GameState game) {
		super.executeAction(game);
		
	}
	
	public boolean isLegalMove(Map map, AgentAction aa) {
		int x = getX();
		int y = getY();
		
		switch(aa) {
		case MOVE_UP: 
			y --;
			break;
		case MOVE_DOWN:
			y ++;
			break;
		case MOVE_LEFT:
			x--;
			break;
		case MOVE_RIGHT:
			x++;
			break;
		case STOP:
			break;
		case PUT_BOMB:
			break;
		default :
			break;
		}
		
		if(map.get_walls()[x][y] || map.getStart_brokable_walls()[x][y] )
			return false;
		else return true;
		
	}

}
