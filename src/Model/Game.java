package Model;
import java.util.Observable;

public abstract class Game extends Observable implements Runnable{
	private int turn;

	private int maxturn = 500;
	private boolean isRunning;
	private Thread thread;
	private double default_time = 300;
	private double time =default_time;
		
	//Initialise le jeu
	public void init() {
		this.turn = 0;
		this.isRunning=true;
		initializeGame();
	}

	
	//Effectue un tour de jeu
	public void step() {
		if(gameContinue()) {
			++turn;
			takeTurn();
		}
		else {
			isRunning=false;
			gameOver();
		}
		this.setChanged();
		this.notifyObservers();
	}

	//Lance le jeu en tour par tour 
	public void run() {
		while(isRunning) {
			step();
      try {
				Thread.sleep((long) time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
		
	
	
	//Met le jeu en pause
	public void stop() {
		isRunning = false;
	}
	
	
	//Lancement du thread
	public void launch() {
		isRunning=true;
		thread = new Thread(this);
		thread.start();
	}
	
	
	//Controle le nombre de tours par secondes
	public void setTime(double coef) {
		this.time=default_time/coef;
	}
	
	public abstract boolean gameContinue();		//Vérifie que le jeu soit fini ou non
	public abstract void gameOver();			//Affiche le message de fin du jeu
	public abstract void takeTurn();			
	public abstract void initializeGame();		//Initialise le jeu en sous classe
	
	
	
	//#################################################
	//				Getters et Setters
	public int getTurn() {
		return turn;
	}


	public void setTurn(int turn) {
		this.turn = turn;
	}


	public int getMaxturn() {
		return maxturn;
	}


	public void setMaxturn(int maxturn) {
		this.maxturn = maxturn;
	}


	public boolean isRunning() {
		return isRunning;
	}


	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
	//#################################################
	
}
