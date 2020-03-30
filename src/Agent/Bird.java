package Agent;

import Controler.GameState;
import Strategies.Strategy;
import Strategies.StrategyBird;
import Controler.Map;

public class Bird extends Agent {

	private boolean endormie = true;

	private StrategyBird strategyBird = new StrategyBird();

	public Bird(int x, int y, AgentAction agentAction, Strategy strategy) {

		super(x, y, agentAction, 'V', ColorAgent.DEFAULT, false, false, strategy);
	}

	public void executeAction(GameState game) {
		super.executeAction(game);
		
		int x = getX();
		int y = getY();
		
		switch(getAgentAction()) {
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
			setX(x);
			setY(y);
		
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

		if(map.getStart_brokable_walls()[x][y] )
			return true;
		else if(map.get_walls()[x][y] ){
			return false;
		}
		else return true;
		
	}

	public boolean isEndormie() {
		return endormie;
	}

	public void setEndormie(boolean endormie) {
		this.endormie = endormie;
	}

	public StrategyBird getStrategyBird() {
		return strategyBird;
	}

	public void setStrategyBird(StrategyBird strategyBird) {
		this.strategyBird = strategyBird;
	}

}
