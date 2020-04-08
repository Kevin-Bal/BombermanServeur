package Strategies;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
        AgentAction ac = action;
        action = AgentAction.STOP;
        if(bomberman.isLegalMove(game.getMap(),game.getBombermans(), ac)){
            return ac;
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
