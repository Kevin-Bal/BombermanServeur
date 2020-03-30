package Strategies;

import java.util.Random;

import Agent.Agent;
import Agent.AgentAction;
import Agent.Bomberman;
import Controler.GameState;

public class StrategyBombermanRandom implements Strategy{

	@Override
	public AgentAction chooseAction(Agent agent, GameState game) {

        Bomberman bomberman = (Bomberman) agent;
        AgentAction aa = GenerateRandomMove();
        if(bomberman.isLegalMove(game.getMap(),game.getBombermans(), aa))
            return aa;
        else return  AgentAction.STOP;
	}
	
	public AgentAction GenerateRandomMove() {
	    int pick = new Random().nextInt(AgentAction.values().length); 
	    return AgentAction.values()[pick];
	}
	
}
