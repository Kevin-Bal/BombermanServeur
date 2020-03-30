package Strategies;

import Agent.Agent;
import Agent.AgentAction;
import Agent.Enemy;
import Controler.GameState;

/*
 * Cette stratégie fait tourner l'ennemi en carré de TAILLE_DU_CARRE x TAILLE_DU_CARRE (ici on a mis 2x2) 
 */

public class StrategyEnemy implements Strategy {
	int direction=1;
	int TAILLE_DU_CARRE = 2;
	
    @Override
    public AgentAction chooseAction(Agent agent, GameState game) {
        Enemy enemy = (Enemy) agent;
        AgentAction aa = null;
        
        //Pour faire tourner l'ennemie en carré
        switch((direction/TAILLE_DU_CARRE)) {
        case 1 : aa = AgentAction.MOVE_RIGHT;
        	break;
        case 2 : aa = AgentAction.MOVE_DOWN;
    		break;
        case 3 : aa = AgentAction.MOVE_LEFT;
    		break;
        case 4 : aa = AgentAction.MOVE_UP;
    		break;
        default : aa = AgentAction.MOVE_RIGHT;
        	break;
        }
        
        if(direction==4*TAILLE_DU_CARRE+1)
        	direction=1;

        ++direction;
        
        if(enemy.isLegalMove(game.getMap(), aa)) return aa;
        else return  AgentAction.STOP;
    }
}
