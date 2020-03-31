package Serveur;/* On  importe les  classes  Reseau, Entrees Sorties, Utilitaires */
import Agent.Agent;
import Controler.ControleurBombermanGame;
import Controler.GameState;
import Controler.Map;
import Model.BombermanGame;
import Strategies.Strategy;
import Strategies.StrategyBombermanRandom;

import java.net.*;
import java.io.*;
import java.util.*;


public class ServiceClient implements Runnable, Observer {
    private  Socket ma_connection;
    private int reponse;
    private ArrayList<Socket> clients = new ArrayList<>();
    private Map map;
    private BombermanGame game = null;
    private ArrayList<Strategy> objets_strategies = new ArrayList<>();
    private ControleurBombermanGame controleurBombermanGame;
    private PrintWriter ma_sortie = null;
    private ObjectOutputStream objectOutputStream = null;

    // Elements envoyés par le client vers le serveur poour initialiser des attributs important
    private  String nomClient = "";
    private  String email = "";
    private  String mdp = "";
    private  String nomMap;

    // Constante projet :
    private static final String HELP = "/help";
    private static final String CLASSEMENT = "/classement";
    private static final String PARTIE = "/partie";
    private static final String QUITTER = "/quit";

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
        int nombre_bbm = 0;

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


        String  message_lu = new String();

        // Initialisation du nom du client
        try {
            nomClient = flux_entrant.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("[Serveur]: Connexion de : "+ nomClient );

        if(!nomClient.equals("")){
            ma_sortie.println("Quel est votre ad mail :");
            try {
                this.email = flux_entrant.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ma_sortie.println("Quel est votre mdp :");
            try {
                this.mdp = flux_entrant.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            for(int i = 0 ; i < nombre_bbm; i++)
                objets_strategies.add(new StrategyBombermanRandom());

            game.getEtatJeu().setStrategies_bombermans(objets_strategies);

            game.launch();

        }

    }

    @Override
    public void update(Observable observable, Object o) {
        System.out.println("Tours : "+Integer.toString(game.getTurn()));
        //ma_sortie.println("Tour : " + game.getTurn());
        for(Agent a : game.getEtatJeu().getBombermans())
            ma_sortie.println("Agent "+a.getType()+" : x("+a.getX()+") y("+a.getY()+")");

        /*try {
            objectOutputStream.writeObject(game.getMap());
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}