package Strategies;

import java.io.Serializable;

import Agent.Agent;
import Agent.AgentAction;
import Agent.Bomberman;
import Controler.GameState;

public class StrategyBombermanInteractif implements Strategy, Serializable {

    AgentAction action;

    //Par défaut, le Bomberman est à l'arrêt au départ
    public StrategyBombermanInteractif() {
        action = AgentAction.STOP;
    }

    @Override
    public AgentAction chooseAction(Agent agent, GameState game) {
        Bomberman bomberman = (Bomberman) agent;
        if(bomberman.isLegalMove(game.getMap(),game.getBombermans(), action)){
            return action;
        }
        else return  AgentAction.STOP;
    }

    public AgentAction getAction() {
        return action;
    }

    public void setAction(AgentAction action) {
        this.action = action;
    }

}
