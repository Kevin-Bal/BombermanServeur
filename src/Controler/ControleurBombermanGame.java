package Controler;

import Model.BombermanGame;
import Strategies.Strategy;

import java.util.ArrayList;


public class ControleurBombermanGame implements InterfaceControleur {

	private BombermanGame game;
	
	public ControleurBombermanGame(BombermanGame g) throws Exception {
		this.game=g;
	}
	
	@Override
	public void start() {
		game.launch();
	}

	@Override
	public void step() {
		game.step();
	}

	@Override
	public void run() {
		game.run();
	}

	@Override
	public void stop() {
		game.stop();
	}
	
	@Override
	public void restart() {
		Map map = null;
		try {
			map = new Map(game.getMap().getFilename());
		} catch (Exception e) {
			e.printStackTrace();
		}
		game.setMap(map);
		ArrayList<Strategy> strategies =game.getEtatJeu().getStrategies_bombermans();
		game.init();
		game.getEtatJeu().setStrategies_bombermans(strategies);
		game.launch();
	}
	
	@Override
	public void setTime(double time) {
		game.setTime(time);
	}



}
