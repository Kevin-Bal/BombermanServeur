package Serveur;

import Agent.Agent;
import Item.InfoBomb;
import Item.InfoItem;

import java.util.ArrayList;

public class ServerObject {

    private boolean[][] breakable_walls ;
    private ArrayList<Agent> listInfoAgents;
    private ArrayList<InfoItem> listInfoItems;
    private ArrayList<InfoBomb> listInfoBombs;

    public boolean[][] getBreakable_walls() {
        return breakable_walls;
    }

    public void setBreakable_walls(boolean[][] breakable_walls) {
        this.breakable_walls = breakable_walls;
    }

    public ArrayList<Agent> getListInfoAgents() {
        return listInfoAgents;
    }

    public void setListInfoAgents(ArrayList<Agent> listInfoAgents) {
        this.listInfoAgents = listInfoAgents;
    }

    public ArrayList<InfoItem> getListInfoItems() {
        return listInfoItems;
    }

    public void setListInfoItems(ArrayList<InfoItem> listInfoItems) {
        this.listInfoItems = listInfoItems;
    }

    public ArrayList<InfoBomb> getListInfoBombs() {
        return listInfoBombs;
    }

    public void setListInfoBombs(ArrayList<InfoBomb> listInfoBombs) {
        this.listInfoBombs = listInfoBombs;
    }

    public void setInfoGame(boolean[][] breakable_walls, ArrayList<Agent> listInfoAgents, ArrayList<InfoItem> listInfoItems, ArrayList<InfoBomb> listInfoBombs){
        setBreakable_walls(breakable_walls);
        setListInfoAgents(listInfoAgents);
        setListInfoItems(listInfoItems);
        setListInfoBombs(listInfoBombs);
    }
}
