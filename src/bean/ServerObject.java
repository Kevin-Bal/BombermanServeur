package bean;

import java.io.Serializable;
import java.util.ArrayList;

import Item.InfoItem;

public class ServerObject implements Serializable{
    /**
     *
     */
    private boolean[][] breakable_walls ;
    private ArrayList<String> listInfoAgents = new ArrayList<>();;
    private ArrayList<InfoItem> listInfoItems = new ArrayList<>();
    private ArrayList<String> listInfoBombs = new ArrayList<>();;
    private boolean gameDone = false;
    private String gameMode = "";

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }



    public boolean isGameDone() {
        return gameDone;
    }

    public void setGameDone(boolean gameDone) {
        this.gameDone = gameDone;
    }

    private static final long serialVersionUID = 1L;
    public ServerObject() { }

    public boolean[][] getBreakable_walls() {
        return breakable_walls;
    }

    public void setBreakable_walls(boolean[][] breakable_walls) {
        this.breakable_walls = breakable_walls;
    }
    public ArrayList<InfoItem> getListInfoItems() {
        return listInfoItems;
    }

    public void setListInfoItems(ArrayList<InfoItem> listInfoItems) {
        for(InfoItem s : listInfoItems) {
            this.listInfoItems.add(s);
        }
    }
    public ArrayList<String> getListInfoBombs() {
        return listInfoBombs;
    }

    public void setListInfoBombs(ArrayList<String> listInfoBombs) {
        for(String s : listInfoBombs) {
            this.listInfoBombs.add(s);
        }
    }

    public ArrayList<String> getListInfoAgents() {
        return listInfoAgents;
    }

    public void setListInfoAgents(ArrayList<String> listInfoAgents) {
        this.listInfoAgents = listInfoAgents;
    }

    public void setInfoGame(boolean[][] breakable_walls, ArrayList<String> listInfoAgents, ArrayList<InfoItem> listInfoItems, ArrayList<String> listInfoBombs, boolean gameDone, String gameMode){
        setBreakable_walls(breakable_walls);
        setListInfoAgents(listInfoAgents);
        setListInfoItems(listInfoItems);
        setListInfoBombs(listInfoBombs);
        setGameDone(gameDone);
        setGameMode(gameMode);
    }
}