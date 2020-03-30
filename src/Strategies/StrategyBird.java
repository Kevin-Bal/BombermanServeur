package Strategies;

import Agent.Agent;
import Agent.AgentAction;
import Controler.GameState;
import Agent.Bird;
//import com.sun.corba.se.spi.legacy.connection.GetEndPointInfoAgainException;

import java.util.ArrayList;
import java.util.Random;

public class StrategyBird implements Strategy {

    private ArrayList<AgentAction> actions = new ArrayList<>();

    @Override
    public AgentAction chooseAction(Agent agent, GameState game) {

        ArrayList<Agent> bombermans = game.getBombermans();
        ArrayList<AgentAction> actions_strat = new ArrayList<>() ;
        actions.add(AgentAction.MOVE_DOWN);
        actions.add(AgentAction. MOVE_LEFT);
        actions.add(AgentAction.STOP);
        actions.add(AgentAction.MOVE_RIGHT);
        actions.add(AgentAction.MOVE_UP);

        Bird bird = (Bird) agent;

        AgentAction action = null;;

        int x = bird.getX();
        int y = bird.getY();


        for(AgentAction act : actions){
            int ax = 0;
            int ay = 0;

            switch(act) {
                case MOVE_UP:
                    ay --;
                    break;
                case MOVE_DOWN:
                    ay ++;
                    break;
                case MOVE_LEFT:
                    ax --;
                    break;
                case MOVE_RIGHT:
                    ax ++;
                    break;
                case STOP:
                    break;
                default :
                    break;
            }


            if(bird.isEndormie()) {

                for(Agent bomberman : bombermans) {
                    int xb = bomberman.getX();
                    int yb = bomberman.getY();

                    int xecb = Math.abs(xb-x);
                    int yecb = Math.abs(yb-y);
                    int becart = xecb + yecb;

                    if(becart < 5) {
                        bird.setEndormie(false);

                    }
                }
                return AgentAction.STOP;
            }
            else {
                int new_ec = 0;
                int aux_ecart = 0;
                int ecart = 40000;
                	for(Agent bomberman: bombermans) {

                        int xb = bomberman.getX();
                        int yb = bomberman.getY();

                        int xecb = Math.abs(xb-x);
                        int yecb = Math.abs(yb-y);
                        aux_ecart = xecb + yecb;


                        if(aux_ecart < ecart) {
                            ecart = aux_ecart;
                            int depx = Math.abs(x+ax-xb);
                            int depy = Math.abs(y+ay-yb);
                            new_ec = depx + depy;
                        }

                        if(new_ec <= ecart){
                            actions_strat.add(act);
                        }

                        if(ecart > 8)
                            bird.setEndormie(true);
                    }
            }
        }


        if(bombermans.size()>0) {
            AgentAction act = actions_strat.get((int) (Math.random() * actions_strat.size()));

            if(bird.isLegalMove(game.getMap(), act)) {
                return act;
            }
        }
        else {
            return AgentAction.STOP;
        }

        return AgentAction.STOP;
    }
}
