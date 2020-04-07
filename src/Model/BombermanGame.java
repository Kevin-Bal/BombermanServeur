package Model;

import Controler.GameState;
import Controler.Map;

import java.io.Serializable;

public class BombermanGame extends Game implements Serializable {

	private boolean endgame;
	private Map map;
	private GameMode gameMode;
	public GameState etatJeu;
	
	@Override
	public boolean gameContinue() {
		//System.out.println("Jeu en cours...");
		//Trouver le mode de jeu (PVP/PVE) au début de la partie pour déterminer la fin

		if (this.getMaxturn()>this.getTurn() && etatJeu.getBombermans().size()!=0) {
			if(gameMode==GameMode.PVE && etatJeu.getAgents().size()<=1) {
				System.out.println("Check running : "+etatJeu.getBombermans().size());
				return false;
			}
			if(gameMode==GameMode.PVP && etatJeu.getBombermans().size()==1) {
				return false;
			}

			return true;
		}
		return false;
	}

	@Override
	public void gameOver() {		
		System.out.println("Fin du jeu");
		endgame=true;
	}

	@Override
	public void takeTurn() {		
		this.etatJeu.takeTurn();
		this.notifyObservers();
	}

	@Override
	public void initializeGame() {
		endgame = false;
		this.etatJeu = new GameState(this.map,this);
		findGameMode();
		System.out.println(gameMode);
	}

	public void findGameMode() {
		if(etatJeu.getBombermans().size()==1)
			gameMode=GameMode.PVE;
		else
			gameMode=GameMode.PVP;
	}
	
	//################################################################################
	//			GETTERS AND SETTERS
	public Map getMap() { return map; }
	public void setMap(Map map) { this.map = map; }
	public boolean isEndgame() { return endgame; }
	public void setEndgame(boolean endgame) { this.endgame = endgame; }
	public GameMode getGameMode() { return gameMode;}
	public void setGameMode(GameMode gameMode) { this.gameMode = gameMode;}
	public GameState getEtatJeu() { return etatJeu;	}
	public void setEtatJeu(GameState etatJeu) { this.etatJeu = etatJeu; }
	//################################################################################
}
