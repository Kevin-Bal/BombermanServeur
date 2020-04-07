package Controler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import Agent.Agent;
import Agent.Bomberman;
import Agent.BombermanFactory;
import Agent.EnemyFactory;
import Item.InfoBomb;
import Item.InfoItem;
import Item.ItemType;
import Item.StateBomb;
import Model.BombermanGame;
import Strategies.*;

public class GameState implements Serializable {
	private int MAX_RANDOM_GENERATE_ITEM=20;
	
	private ArrayList<Agent> bombermans = new ArrayList<>();
	private ArrayList<Agent> deadBombermans = new ArrayList<>();
	private ArrayList<Agent> enemies = new ArrayList<>();
	private ArrayList<InfoBomb> bombs = new ArrayList<>();
	private ArrayList<InfoBomb> bombsSupprime = new ArrayList<>();
	private ArrayList<InfoItem> items = new ArrayList<>();
	private ArrayList<Strategy> strategies_bombermans = new ArrayList<>();
	private boolean brokable_walls[][];

	private BombermanGame game;
	private Map map;
	
	public GameState(Map map, BombermanGame game) {
		System.out.println("Création de GameState");
		this.game = game;
		this.map = map;
		
		ArrayList<Agent> agents = map.getStart_agents();
		BombermanFactory bFactory = new BombermanFactory();
		EnemyFactory eFactory = new EnemyFactory();
		brokable_walls = map.getStart_brokable_walls();

		
		for(Agent a : agents) {
			switch(a.getType()) {
			case 'B':
				bombermans.add( bFactory.createAgent(a.getX(), a.getY(), a.getType(), a.getAgentAction(), a.getColor(), null));
				break;
			case 'R':
				enemies.add( eFactory.createAgent(a.getX(), a.getY(), a.getType(), a.getAgentAction(), a.getColor(), new StrategyRajion()));
				break;
			case 'V':
				enemies.add( eFactory.createAgent(a.getX(), a.getY(), a.getType(), a.getAgentAction(), a.getColor(), new StrategyBird()));
				break;
			case 'E':
				enemies.add( eFactory.createAgent(a.getX(), a.getY(), a.getType(), a.getAgentAction(), a.getColor(), new StrategyEnemy()));
				break;
			default:
				break;
				
			}
		}


	}
	
	public void takeTurn() {
		takeTurnEnemies();
		takeTurnBomberman();
		checkIfEnemieIsOnBomberman();
		bombTurn();
		game.notifyObservers();
	}

	
	
	//###########################################################################
	//				TAKE TURNS
	
	/*
	 * Au tour de l'ennemie de jouer
	 */
	public void takeTurnEnemies() {
		ArrayList<Agent> enemieSupprime = new ArrayList<>();
		for (Agent enemie : enemies) {

			enemie.executeAction(this);

			if (enemie.isDead() == true) {
				enemieSupprime.add(enemie);
			}
		}
        
		//Remove dead Enemies from list
		for(Agent a: enemieSupprime) {
			enemies.remove(a);
		}
        
	}
	
	/*
	 * Au tour des bombermans de jouer
	 */
	public void takeTurnBomberman() {
		ArrayList<Agent> bombermanSupprime = new ArrayList<>();
		for (Agent bomberman : bombermans) {

			Bomberman b = (Bomberman) bomberman;
			b.checkForItem(items);

			b.executeAction(this);
				
			//Si il est mort
			if(b.isDead()==true) {
				bombermanSupprime.add(b);
				deadBombermans.add(b);
			}
		}
		
		//Remove dead Enemies from list
		for(Agent a: bombermanSupprime) {
			bombermans.remove(a);
		}
	}
	
	public void bombTurn(){
		for(InfoBomb bomb : bombs) {
			switch(bomb.getStateBomb()) {
			case Step1 :
				bomb.setStateBomb(StateBomb.Step2);
				break;
			case Step2 :
				bomb.setStateBomb(StateBomb.Step3);
				break;
			case Step3 :
				bomb.setStateBomb(StateBomb.Boom);
				break;
			case Boom :
				bombsSupprime.add(bomb);
				isLegalExplosion(bomb);
					
				break;
			default :
				break;
			}
		}
		
		for(InfoBomb bomb : bombsSupprime) {
			bombs.remove(bomb);
		}
		bombsSupprime.clear();
	}
	
	//###########################################################################
	//				CHECKS
	private void checkIfEnemieIsOnBomberman() {
		for(Agent bomberman : bombermans) {
			for(Agent enemie : enemies) {
				if(bomberman.getX()==enemie.getX() && bomberman.getY()==enemie.getY())
					bomberman.setDead(true);
			}
		}
	}
	public void checkIfEnemiesAndBombermansAreTouchedByFlames(int x, int y,InfoBomb bomb) {
		for(Agent bomberman : bombermans) {
			if(bomberman.getX()==x && bomberman.getY()==y && !bomberman.isInvincible() && bomberman.getId()!=bomb.getBomberman().getId()) {
				bomberman.setDead(true);
				System.out.print(bomb.getBomberman().getId()+"	"+bomb.getBomberman().score);
				bomb.getBomberman().score += 200;
				System.out.println("	"+bomb.getBomberman().score);
			}
			break;
		}
		for(Agent enemie : enemies) {
			if(enemie.getX()==x && enemie.getY()==y && !enemie.isInvincible()) {
				enemie.setDead(true);
				bomb.getBomberman().score += 100;
				break;
			}
		}
	}
	//###########################################################################
	//				IS LEGAL EXPLOSION
	/*
	 * Check if a wall is broken or an enemy is killed by a flame 
	 */
	public void isLegalExplosion(InfoBomb bomb) {
		int x = bomb.getX();
		int y = bomb.getY();
		int range = bomb.getRange();

		for(int i = x; i <= x+range; i++ ) {
			
			if( i > 0 && i < map.getSizeX() ) {
				if(map.get_walls()[i][y]) {
					break;
				}
				if(this.brokable_walls[i][y]) {
					this.brokable_walls[i][y] = false;
					if(this.GenerateRandomNumber()<MAX_RANDOM_GENERATE_ITEM) {
						ItemType type = GenerateRandomItem();
						items.add(new InfoItem(i,y,type));
					}
					break;
				}
				checkIfEnemiesAndBombermansAreTouchedByFlames(i,y,bomb);
			}
			
		}
		
		for(int i = x; i >= x-range; i--) {
			
			if( i > 0 && i < map.getSizeX() ) {
				if(map.get_walls()[i][y]) {
					break;
				}
				if(this.brokable_walls[i][y]) {
					this.brokable_walls[i][y] =false;
					if(this.GenerateRandomNumber()<MAX_RANDOM_GENERATE_ITEM) {
						ItemType type = GenerateRandomItem();
						items.add(new InfoItem(i,y,type));
					}
					break;
				}
				checkIfEnemiesAndBombermansAreTouchedByFlames(i,y,bomb);
			}
		}
		
		for(int i = y; i <= y+range; i++ ) {
			
			if( i > 0 && i < map.getSizeY() ) {
				if(map.get_walls()[x][i]) {
					break;
				}
				if(this.brokable_walls[x][i]) {
					this.brokable_walls[x][i] =false;
					if(this.GenerateRandomNumber()<MAX_RANDOM_GENERATE_ITEM) {
						ItemType type = GenerateRandomItem();
						items.add(new InfoItem(x,i,type));
					}
					break;
				}
				checkIfEnemiesAndBombermansAreTouchedByFlames(x,i,bomb);
			}
		}
		
		for(int i = y; i >= y-range; i-- ) {
			
			if( i > 0 && i < map.getSizeY() ) {
				if(map.get_walls()[x][i]) {
					break;
				}
				if(this.brokable_walls[x][i]) {
					this.brokable_walls[x][i] =false;
					if(this.GenerateRandomNumber()<MAX_RANDOM_GENERATE_ITEM) {
						ItemType type = GenerateRandomItem();
						items.add(new InfoItem(x,i,type));
					}
					break;
				}
				checkIfEnemiesAndBombermansAreTouchedByFlames(x,i,bomb);
			}
		}
	}	
	
	//###########################################################################
	//				GENERATE RANDOM	
	public ItemType GenerateRandomItem() {
	    int pick = new Random().nextInt(ItemType.values().length); 
	    return ItemType.values()[pick];
	}

	public int GenerateRandomNumber() {
	    int pick = new Random().nextInt(100); 
	    return pick;
	}
	//###########################################################################
	//				GETTERS AND SETTERS
	public ArrayList<Agent> getBombermans() {
		return bombermans;
	}

	public void setBombermans(ArrayList<Agent> bombermans) {
		this.bombermans = bombermans;
	}

	public ArrayList<Agent> getEnemies() {
		return enemies;
	}

	public void setEnemies(ArrayList<Agent> enemies) {
		this.enemies = enemies;
	}
	
	public ArrayList<Agent> getAgents(){
		ArrayList<Agent> all = new ArrayList<>();
		all.addAll(this.getBombermans());
		all.addAll(this.getEnemies());
		return all;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}
	
	public ArrayList<InfoBomb> getBombs() {
		return bombs;
	}

	public void setBombs(ArrayList<InfoBomb> bombs) {
		this.bombs = bombs;
	}
	
	public boolean[][] getBrokable_walls() {
		return brokable_walls;
	}

	public void setBrokable_walls(boolean[][] brokable_walls) {
		this.brokable_walls = brokable_walls;
	}


	public ArrayList<InfoItem> getItems() {
		return items;
	}


	public void setItems(ArrayList<InfoItem> items) {
		this.items = items;
	}

	public ArrayList<Agent> getDeadBombermans() {
		return deadBombermans;
	}

	public void setDeadBombermans(ArrayList<Agent> deadBombermans) {
		this.deadBombermans = deadBombermans;
	}

	public ArrayList<Strategy> getStrategies_bombermans() {
		return strategies_bombermans;
	}

	public void setStrategies_bombermans(ArrayList<Strategy> strategies_bombermans) {
		this.strategies_bombermans = strategies_bombermans;
		for(int i = 0; i < bombermans.size(); i++){
			Strategy strat = strategies_bombermans.get(i);
			bombermans.get(i).setStrategy(strat);
		}
	}

	//###########################################################################


	
}
