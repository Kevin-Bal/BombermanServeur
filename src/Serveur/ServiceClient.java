package Serveur;/* On  importe les  classes  Reseau, Entrees Sorties, Utilitaires */
import Agent.AgentAction;
import Agent.Bomberman;
import ApiClient.ClientHistoriqueService;
import ApiClient.ClientUtilisateurService;
import ApiClient.Historique;
import ApiClient.Utilisateur;
import Controler.ControleurBombermanGame;
import Controler.Map;
import Item.InfoItem;
import Model.BombermanGame;
import Strategies.Strategy;
import Strategies.StrategyBomberman;
import Strategies.StrategyBombermanInteractif;
import Strategies.StrategyBombermanRandom;
import bean.ServerObject;
import com.google.gson.Gson;
import org.apache.commons.lang3.SerializationUtils;
import java.net.*;
import java.io.*;
import java.util.*;


public class ServiceClient implements Runnable, Observer {
    private  Socket ma_connection;
    private ArrayList<Socket> clients = new ArrayList<>();
    private Map map;    
    private BombermanGame game = null;
    private ArrayList<Strategy> objets_strategies = new ArrayList<Strategy>();
    private PrintWriter ma_sortie = null;
    private BufferedReader flux_entrant = null;
    private ServerObject sendObject = new ServerObject();
    private ClientUtilisateurService user = new ClientUtilisateurService();
    private Utilisateur joueur = null;
    private int nombre_bbm = 0;

    // Elements envoyés par le client vers le serveur poour initialiser des attributs important
    private  String nomClient = "";
    private  String email = "";
    private  String mdp = "";
    private  String nomMap = "";

    // Constante projet :
    private final static  String QUIT = "quitter";
    private final static String START = "start";
    private final static String PAUSE = "pause";
    private final static String RESTART = "restart";
    private final static String STEP = "step";
    private final static String MOVE_RIGHT = "d";
    private final static String MOVE_LEFT = "q";
    private final static String MOVE_UP = "z";
    private final static String MOVE_DOWN = "s";
    private final static String PUT_BOMB = "f";

    public ServiceClient(Socket la_connection, ArrayList<Socket> clients){
        ma_connection = la_connection;
        this.clients = clients;
        game = new BombermanGame();
    }

    private void terminer(){
        try{
            if (ma_connection != null) {
                System.out.format("Deconnexion pour %s\n", nomClient);
                ma_connection.close();
                ma_sortie.close();
                flux_entrant.close();
            }
        }
        catch (IOException e) {
            System.out.format("Déconnexion pour %s\n", nomClient);
            e.printStackTrace();
        }
    }

    public  void run(){
        // Phase d initialisation

        try {
            InputStreamReader isr = new InputStreamReader(ma_connection.getInputStream(), "UTF-8");
            flux_entrant = new BufferedReader(isr);
            ma_sortie = new PrintWriter(ma_connection.getOutputStream(), true);
        }
        catch (Exception e) {
            System.out.println("Erreur d initialisation") ;
            e.printStackTrace();
        }



        // Initialisation du nom du client
        String connexion = "";
        while(nomClient.equals("")){
            try {
                connexion = flux_entrant.readLine();

                email = connexion;
                mdp = flux_entrant.readLine();
                /*if(connexion.equals(QUIT)){

                    break;
                }*/
                joueur = user.getUtilisateur(email, mdp);
                if(joueur == null){
                    ma_sortie.println("[Serveur]: Mauvais mot de passe ou adresse mail : réessayer !");
                }
                else
                    nomClient = joueur.getUserName();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /*
        if(connexion.equals(QUIT)){
            terminer();
            return;
        }*/

        System.out.println("[Serveur]: Connexion de : "+ nomClient );
        ma_sortie.println("[Serveur]: Bienvenue");


        // Initialisation du nom de la map
        while(nomMap.equals("")) {
            try {
                this.nomMap = flux_entrant.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        try {
			this.map = new Map(nomMap);
	        this.game.setMap(map);
	        this.game.addObserver(this);
	        game.setTime(5);
	        game.init();
	        //Construction du panneau des stratégies
	        nombre_bbm = game.getEtatJeu().getBombermans().size();
		} catch (Exception e1) {e1.printStackTrace();}

        System.out.println("Initialisation réussie !");
        
        
        //Initialisation du choix des strategies
        ma_sortie.println(nombre_bbm);
        for(int i = 0 ; i < nombre_bbm; i++) {
        	try {
				switch(flux_entrant.readLine()) {
					case "Bomberman IA 1":
					    objets_strategies.add(new StrategyBomberman());
					    break;
					case "Bomberman Aléatoire":
					    objets_strategies.add(new StrategyBombermanRandom());
					    break;
					case "Bomberman Interactif":
					    objets_strategies.add(new StrategyBombermanInteractif());
					    break;
				}
				
			} catch (IOException e) {e.printStackTrace();}
        }

        
        
        String message_lu="";
		while(game.gameContinue() || !game.isEndgame()){
            try {
                message_lu = flux_entrant.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            switch(message_lu){
                case START :
                    game.getEtatJeu().setStrategies_bombermans(objets_strategies);
                    game.launch();
                    break;

                case PAUSE :
                    game.stop();
                    break;

                case RESTART :
                    try {
                        new Map(nomMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    game.init();
                    game.getEtatJeu().setStrategies_bombermans(objets_strategies);
                    game.launch();
                    break;

                case STEP :
                    game.step();
                    break;


            }

            if(game.getEtatJeu().getBombermans().get(0).getStrategy() instanceof StrategyBombermanInteractif){
                switch(message_lu){
                    case MOVE_DOWN :
                        ((StrategyBombermanInteractif) game.getEtatJeu().getBombermans().get(0).getStrategy()).setAction(AgentAction.MOVE_DOWN);
                        break;

                    case MOVE_LEFT :
                        ((StrategyBombermanInteractif) game.getEtatJeu().getBombermans().get(0).getStrategy()).setAction(AgentAction.MOVE_LEFT);
                        break;

                    case MOVE_RIGHT :
                        ((StrategyBombermanInteractif) game.getEtatJeu().getBombermans().get(0).getStrategy()).setAction(AgentAction.MOVE_RIGHT);
                        break;

                    case MOVE_UP :
                        ((StrategyBombermanInteractif) game.getEtatJeu().getBombermans().get(0).getStrategy()).setAction(AgentAction.MOVE_UP);
                        break;

                    case PUT_BOMB :
                        ((StrategyBombermanInteractif) game.getEtatJeu().getBombermans().get(0).getStrategy()).setAction(AgentAction.PUT_BOMB);
                        break;
                }
            }



	     } 
}

    @Override
    public void update(Observable observable, Object o) {
        BombermanGame gameCourant =  SerializationUtils.clone(this.game);

        ArrayList<String> infoBombs = new ArrayList<>();
        ArrayList<String> infoAgents = new ArrayList<>();
        ArrayList<InfoItem> infoItems = new ArrayList<>();
        boolean gameDone = gameCourant.isEndgame();
        infoItems.addAll(infoItems);

        gameCourant.getEtatJeu().getBombs().stream().forEach(bomb -> infoBombs.add(bomb.toText()));
        gameCourant.getEtatJeu().getAgents().stream().forEach(agent -> infoAgents.add(agent.toText()));

        
        this.sendObject.setInfoGame(gameCourant.getEtatJeu().getBrokable_walls(), infoAgents, infoItems, infoBombs, gameDone);
        Gson gson          = new Gson();
        String posting = gson.toJson(this.sendObject);

        ma_sortie.println(posting);
        
        if(gameCourant.isEndgame()) {
   		 //Envoi du résultat à l'API
   		 Bomberman bbm ;
            if(gameCourant.getEtatJeu().getBombermans().size() > 0)
                bbm =(Bomberman) gameCourant.getEtatJeu().getBombermans().get(0);
            else
                bbm = (Bomberman) gameCourant.getEtatJeu().getDeadBombermans().get(0);

            Historique histo = new Historique();
            histo.setUsernameJoueur(nomClient);
            histo.setVictoire("defaite");
            histo.setScore(bbm.score);
            histo.setNbJoueur(nombre_bbm);
            histo.setModeJeu(gameCourant.getGameMode().toString());
            histo.setEmailJoueur(email);
            histo.setMapName(nomMap);

            ClientHistoriqueService serviceHisto = new ClientHistoriqueService();

            try {
                serviceHisto.postHistorique(histo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}