package Serveur;/* On  importe les  classes  Reseau, Entrees Sorties, Utilitaires */
import Agent.Bomberman;
import ApiClient.ClientHistoriqueService;
import ApiClient.ClientUtilisateurService;
import ApiClient.Historique;
import ApiClient.Utilisateur;
import Controler.ControleurBombermanGame;
import Controler.GameState;
import Controler.Map;
import Item.InfoItem;
import Model.BombermanGame;
import Model.Game;
import Strategies.Strategy;
import Strategies.StrategyBombermanRandom;
import bean.ServerObject;
import com.google.gson.Gson;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.http.entity.StringEntity;

import java.lang.reflect.Array;
import java.net.*;
import java.io.*;
import java.util.*;


public class ServiceClient implements Runnable, Observer {

    private  Socket ma_connection;
    private int reponse;
    private ArrayList<Socket> clients = new ArrayList<>();
    private Map map;    private BombermanGame game = null;
    private ArrayList<Strategy> objets_strategies = new ArrayList<>();
    private ControleurBombermanGame controleurBombermanGame;
    private PrintWriter ma_sortie = null;
    private ObjectOutputStream objectOutputStream = null;
    private ServerObject sendObject = new ServerObject();
    private ClientUtilisateurService user = new ClientUtilisateurService();
    private Utilisateur joueur = null;
    private int nombre_bbm = 0;

    // Elements envoyés par le client vers le serveur poour initialiser des attributs important
    private  String nomClient = "";
    private  String email = "";
    private  String mdp = "";
    private  String nomMap;

    // Constante projet :
    private final static String START = "start";
    private final static String STOP = "stop";
    private final static String RESTART = "restart";

    public ServiceClient(Socket la_connection, ArrayList<Socket> clients){
        ma_connection = la_connection;
        this.clients = clients;
        this.nomMap = "layouts/alone.lay";
        game = new BombermanGame();
    }

    private void terminer(){
        try{
            if (ma_connection != null) {
                System.out.format("Deconnexion pour %s\n", nomClient);
                ma_connection.close();
            }
        }
        catch (IOException e) {
            System.out.format("Déconnexion pour %s\n", nomClient);
            e.printStackTrace();
        }
        return;
    }

    public  void run(){
        // Phase d initialisation
        BufferedReader flux_entrant = null;
        OutputStream outputStream = null;


        try {
            outputStream = ma_connection.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
            InputStreamReader isr = new InputStreamReader(ma_connection.getInputStream(), "UTF-8");
            flux_entrant = new BufferedReader(isr);
            ma_sortie = new PrintWriter(ma_connection.getOutputStream(), true);

            this.map = new Map(nomMap);
            this.game.setMap(map);
            this.game.addObserver(this);
            game.setTime(5);
            game.init();
            //Construction du panneau des stratégies
            nombre_bbm = game.getEtatJeu().getBombermans().size();

            System.out.println("Initialisation réussie !");
        }
        catch (Exception e) {
            System.out.println("Erreur d initialisation") ;
            e.printStackTrace();
        }

        // Initialisation du nom du client
       /* while(nomClient.equals("")){
            try {
                ma_sortie.println("[Serveur]: Quel est votre ad mail :");
                email = flux_entrant.readLine();
                ma_sortie.println("[Serveur]: Quel est votre mot de passe :");
                mdp = flux_entrant.readLine();

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

        System.out.println("[Serveur]: Connexion de : "+ nomClient );
        ma_sortie.println("[Serveur]: Bienvenue : "+ nomClient);*/

        String  message_lu = new String();
        while(true){

            try {
                message_lu = flux_entrant.readLine();


            } catch (IOException e) {
                e.printStackTrace();
            }

            if(message_lu.equals(START)){
                for(int i = 0 ; i < nombre_bbm; i++)
                    objets_strategies.add(new StrategyBombermanRandom());

                game.getEtatJeu().setStrategies_bombermans(objets_strategies);

                game.launch();
            }

            if(message_lu.equals(STOP)){
                ma_sortie.println("Stop");
                game.stop();
            }

            if(message_lu.equals(RESTART)){
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

        }

    }

    @Override
    public void update(Observable observable, Object o) {
        BombermanGame gameCourant =  SerializationUtils.clone(this.game);

        //ma_sortie.println("Tour : "+ this.game.getTurn());
        ArrayList<String> infoBombs = new ArrayList<>();
        ArrayList<String> infoAgents = new ArrayList<>();
        ArrayList<InfoItem> infoItems = new ArrayList<>();
        infoItems.addAll(infoItems);

        gameCourant.getEtatJeu().getBombs().stream().forEach(bomb -> infoBombs.add(bomb.toText()));
        gameCourant.getEtatJeu().getAgents().stream().forEach(agent -> infoAgents.add(agent.toText()));

        this.sendObject.setInfoGame(gameCourant.getEtatJeu().getBrokable_walls(), infoAgents, infoItems, infoBombs, false);
        Gson gson          = new Gson();
        String posting = gson.toJson(this.sendObject);

        ma_sortie.println(posting);

        /*if(gameCourant.isEndgame()){
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

        }*/
        //ma_sortie.println("Tours : "+Integer.toString(game.getTurn()));
        /*try {
            objectOutputStream.writeObject(this.sendObject);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}